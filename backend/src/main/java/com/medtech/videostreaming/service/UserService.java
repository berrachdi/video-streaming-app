package com.medtech.videostreaming.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.videostreaming.Repository.UserRepository;
import com.medtech.videostreaming.Repository.VideoRepository;
import com.medtech.videostreaming.dto.UserInfoDTO;
import com.medtech.videostreaming.dto.VideoDTO;
import com.medtech.videostreaming.maper.MapperClass;
import com.medtech.videostreaming.model.User;
import com.medtech.videostreaming.model.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final VideoService videoService;
    @Value("${auth0.userinfor.endpoint}")
    String userInfoUri;

    public void registerUser(String tokenValue){
        // Send get request to userinfo endpoint of auth0 autorization server
        HttpRequest userInfoRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoUri))
                .setHeader("Autorization", String.format("Bearer %s", tokenValue))
                .build();

        try {

            HttpResponse<String> httpResponse = HttpClient.newBuilder()
                                                .version(HttpClient.Version.HTTP_2)
                                                .build()
                                                .send(userInfoRequest,HttpResponse.BodyHandlers.ofString());
            String body = httpResponse.body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            UserInfoDTO userInfoDTO = objectMapper.readValue(body, UserInfoDTO.class);

            User user = new User();
            user.setFirstName(userInfoDTO.getGivenName());
            user.setLastName(userInfoDTO.getFamilyName());
            user.setFullName(userInfoDTO.getName());
            user.setEmailAddress(userInfoDTO.getEmail());
            user.setSub(userInfoDTO.getSub());

            this.userRepository.save(user);



        } catch (Exception e){
            throw new RuntimeException(e);
        }


        // Save user into the database

    }

    public User getCurrenteUser(){
        String sub = ((Jwt)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("sub");

        User user = this.userRepository.findBySub(sub)
                .orElseThrow(()->new IllegalArgumentException(String.format("Can't find a user with sub: %s",sub)));


        return user;


    }


    public void addToLikedList(String videoid) {
        User currentUser = this.getCurrenteUser();
        currentUser.addToLikedVideos(videoid);
        this.userRepository.save(currentUser);

    }
    public void removeFromLikedList(String videoid) {
        User currentUser = this.getCurrenteUser();
        currentUser.removeFromLikedVideos(videoid);
        this.userRepository.save(currentUser);

    }

    public boolean ifLikedVideo(String videoId){
       User userFinded = this.getCurrenteUser();
       return userFinded.getLikedVideos().stream().anyMatch(likedVideo-> likedVideo.equals(videoId));

    }
    public boolean ifDislikedVideo(String videoId){
        User userFinded = this.getCurrenteUser();
        return userFinded.getDisLikedVideos().stream().anyMatch(likedVideo-> likedVideo.equals(videoId));

    }


    public void removeFromDislikedList(String videoid) {
        User currentUser = this.getCurrenteUser();
        currentUser.removeFromDisLikedVideo(videoid);
        this.userRepository.save(currentUser);
    }

    public void addToDislikeList(String videoid) {
        User currentUser = this.getCurrenteUser();
        currentUser.addToDisLikedVideo(videoid);
        this.userRepository.save(currentUser);
    }

    public void addToHistory(String videoid) {
        User currentUser = this.getCurrenteUser();
        currentUser.addToVideoHistory(videoid);
        this.userRepository.save(currentUser);
    }

    public Set<VideoDTO> getAllVideosHistory() {
        User currentUser = this.getCurrenteUser();
        Set<VideoDTO> videoDTOHistory = new HashSet<>();
        currentUser.getVideoHistory().stream().forEach((videoId)->{
            Video video = this.videoService.findVideo(videoId);
            videoDTOHistory.add(MapperClass.videoToVideoDTO(video));


        });

        return videoDTOHistory;

    }

    public String subscribeToUser(String userid) {
        User currentUser = this.getCurrenteUser();
        if(currentUser.isSubscrubedTo(userid)){
            // In this case we need to delete this user from the list
            // of subsucriberToUser of the current user

            currentUser.removeFromSubscribedToUsers(userid);




            // Get other user from database
            User otherUser = this.userRepository.findById(userid).get();

            // Delete the current user from his subsucribers list
            otherUser.removeFromSubscribers(currentUser.getId());

            // save the changing
            this.userRepository.save(otherUser);
            this.userRepository.save(currentUser);
            return String.format("You're unsubscribed to %s",otherUser.getFullName());


        }else{
            // In this case we need to add this user to the list
            // of subsucriberToUser of the current user

            currentUser.addToSubscribedUsers(userid);




            // Get other user from database
            User otherUser = this.userRepository.findById(userid).get();

            // add the current user to his subsucribers list
            otherUser.addToSubscribers(currentUser.getId());

            // save the changing
            this.userRepository.save(otherUser);
            this.userRepository.save(currentUser);
            return String.format("You're subscribed to %s",otherUser.getFullName());

        }
    }


}
