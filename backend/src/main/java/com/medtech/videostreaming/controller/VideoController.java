package com.medtech.videostreaming.controller;

import com.medtech.videostreaming.dto.CommentDTO;
import com.medtech.videostreaming.dto.UploadVideoResponse;
import com.medtech.videostreaming.dto.VideoDTO;
import com.medtech.videostreaming.model.Video;
import com.medtech.videostreaming.service.UserService;
import com.medtech.videostreaming.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {

        var uploadVideoResponse = videoService.uploadVideo(file);
        return uploadVideoResponse;

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO updateVideoMetaData(@RequestBody VideoDTO videoDTO) {

        return videoService.updateMetaDataVideo(videoDTO);

    }

    @PostMapping(value = "thumbnails")
    @ResponseStatus(HttpStatus.OK)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file,
                                  @RequestParam("videoid") String videoId) {

        return videoService.uploadThumbnail(file, videoId);

    }

    @GetMapping(value = "{videoid}/video-details")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO videoDetails(@PathVariable String videoid) {
        return videoService.videoDetails(videoid);
    }

    @PostMapping(value = "{videoid}/likes")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO videoLikes(@PathVariable String videoid) {
        return videoService.likeVideo(videoid);
    }

    @PostMapping(value = "{videoid}/dislikes")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO videoDislikes(@PathVariable String videoid) {
        return videoService.likeVideo(videoid);
    }

    /*@PostMapping(value = "{videoid}/history")
    @ResponseStatus(HttpStatus.OK)
    public String videoHistory(@PathVariable String videoid){
        this.userService.addToHistory(videoid);
        return String.format("The video with id:%s is added to the history",videoid);
    }*/

    @PostMapping(value = "/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<VideoDTO> GetVideosHistory() {

        return this.userService.getAllVideosHistory();

    }

    @PostMapping(value = "/{videoid}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Video addCommentToVideo(@PathVariable String videoid, @RequestBody CommentDTO commentDTO) {

        return this.videoService.addCommentToVideo(videoid, commentDTO);

    }

    @GetMapping(value = "/liked")
    @ResponseStatus(HttpStatus.OK)
        public List<VideoDTO> getAllVideosLiked() {

        return this.videoService.getAllVideosLiked();

    }


}


