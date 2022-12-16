package com.medtech.videostreaming.service;

import com.medtech.videostreaming.Repository.CommentRepository;
import com.medtech.videostreaming.Repository.VideoRepository;
import com.medtech.videostreaming.dto.CommentDTO;
import com.medtech.videostreaming.dto.UploadVideoResponse;
import com.medtech.videostreaming.dto.VideoDTO;
import com.medtech.videostreaming.maper.MapperClass;
import com.medtech.videostreaming.model.Comment;
import com.medtech.videostreaming.model.User;
import com.medtech.videostreaming.model.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VideoService {

    final S3service s3service;
    final VideoRepository videoRepository;
    final UserService userService;
    final CommentRepository commentRepository;



    public UploadVideoResponse uploadVideo(MultipartFile file){

        String videoUrl = s3service.uploadVideo(file);
        Video video = new Video();
        video.setUrl(videoUrl);
        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getUrl());

    }


    public VideoDTO updateMetaDataVideo(VideoDTO videoDTO) {
        // find a video meta data from the database
        var video =  this.findVideo(videoDTO.getId());

        // edit the meta data

        video.setVideoStatus(videoDTO.getVideoStatus());
        video.setDescription(videoDTO.getDescription());
        video.setTags(videoDTO.getTags());
        video.setTitle(videoDTO.getTitle());
        video.setThumbnailUrl(videoDTO.getThumbnailUrl());

        // save again the new data
        videoRepository.save(video);

        return videoDTO;

    }

    public String uploadThumbnail(MultipartFile file, String videoId) {


        var video = this.findVideo(videoId);
        var thumbnailUrl = s3service.uploadVideo(file);

        video.setThumbnailUrl(thumbnailUrl);


        // save the video in the database

        videoRepository.save(video);

        return thumbnailUrl;



    }

    public Video findVideo(String videoId){
        return videoRepository.findById(videoId)
                .orElseThrow(()-> new IllegalArgumentException(String.format("The video with id:%s not found!",videoId)));
    }

    public VideoDTO videoDetails(String videoid) {
        Video videoFinded = this.findVideo(videoid);
        // Increase video views (videoViews +1)
        this.increaseVideoViews(videoFinded);
        this.userService.addToHistory(videoid);
        VideoDTO videoDTO = new VideoDTO();

        // Mapping between the Video <-> VideoDTO
        videoDTO.setVideoStatus(videoFinded.getVideoStatus());
        videoDTO.setDescription(videoFinded.getDescription());
        videoDTO.setTitle(videoFinded.getTitle());
        videoDTO.setUrl(videoFinded.getUrl());
        videoDTO.setTags(videoFinded.getTags());
        videoDTO.setThumbnailUrl(videoFinded.getThumbnailUrl());
        videoDTO.setId(videoFinded.getId());

        return videoDTO;

    }

    private void increaseVideoViews(Video video) {
        video.increaseViewCount();
        this.videoRepository.save(video);

    }

    public VideoDTO likeVideo(String videoid) {
        Video videoFinded = this.findVideo(videoid);

        // User liked the video
        if(this.userService.ifLikedVideo(videoid)){

            videoFinded.decreaseLikeCount();
            this.userService.removeFromLikedList(videoid);

        }else if(this.userService.ifDislikedVideo(videoid)) {

            videoFinded.decreaseDisLikeCount();
            videoFinded.increaseLikeCount();
            this.userService.addToLikedList(videoid);
            this.userService.removeFromDislikedList(videoid);

        }else {

            videoFinded.increaseLikeCount();
            this.userService.addToLikedList(videoid);
        }





        // Save all video change
        this.videoRepository.save(videoFinded);




        VideoDTO videoDTO = new VideoDTO();

        // Mapping between the VideoFinded <-> VideoDTO
        videoDTO.setVideoStatus(videoFinded.getVideoStatus());
        videoDTO.setDescription(videoFinded.getDescription());
        videoDTO.setTitle(videoFinded.getTitle());
        videoDTO.setUrl(videoFinded.getUrl());
        videoDTO.setTags(videoFinded.getTags());
        videoDTO.setThumbnailUrl(videoFinded.getThumbnailUrl());
        videoDTO.setId(videoFinded.getId());
        videoDTO.setLikeCount(videoFinded.getLikes().get());
        videoDTO.setDislikeCount(videoFinded.getDisLikes().get());

        return videoDTO;


    }
    public VideoDTO dislikedVideo(String videoid) {
        Video videoFinded = this.findVideo(videoid);

        // User liked the video
        if(this.userService.ifDislikedVideo(videoid)){

            videoFinded.decreaseDisLikeCount();
            this.userService.removeFromDislikedList(videoid);

        }else if(this.userService.ifLikedVideo(videoid)) {

            videoFinded.decreaseLikeCount();
            videoFinded.increaseDisLikeCount();
            this.userService.removeFromLikedList(videoid);
            this.userService.addToDislikeList(videoid);

        }else {

            videoFinded.increaseDisLikeCount();
            this.userService.addToDislikeList(videoid);
        }





        // Save all video change
        this.videoRepository.save(videoFinded);




        VideoDTO videoDTO = new VideoDTO();

        // Mapping between the VideoFinded <-> VideoDTO
        videoDTO.setVideoStatus(videoFinded.getVideoStatus());
        videoDTO.setDescription(videoFinded.getDescription());
        videoDTO.setTitle(videoFinded.getTitle());
        videoDTO.setUrl(videoFinded.getUrl());
        videoDTO.setTags(videoFinded.getTags());
        videoDTO.setThumbnailUrl(videoFinded.getThumbnailUrl());
        videoDTO.setId(videoFinded.getId());
        videoDTO.setLikeCount(videoFinded.getLikes().get());
        videoDTO.setDislikeCount(videoFinded.getDisLikes().get());

        return videoDTO;


    }


    public Video addCommentToVideo(String videoid, CommentDTO commentDTO) {

        // Find a video in the data base
           Video currentVideo = this.findVideo(videoid);

        // Add this comment to the list of comment
           Comment newComment = new Comment();
           newComment.setText(commentDTO.getCommentText());
           newComment.setAuthor(this.userService.getCurrenteUser().getId());

           currentVideo.addComment(newComment);
        // update the video
           this.videoRepository.save(currentVideo);

        // Save the comment in the database
           this.commentRepository.save(newComment);

        // Get all comment for the video current from Database


        return currentVideo;
    }

    public List<VideoDTO> getAllVideosLiked() {

        List<VideoDTO> videoDTOList = new ArrayList<>();

        // Find all videos id liked by current user
        userService.getAllVideosliked().stream().forEach((videoId)->{
            VideoDTO videoDTO = MapperClass.videoToVideoDTO(this.findVideo(videoId));
            videoDTOList.add(videoDTO);
        });

        return videoDTOList;


    }
}
