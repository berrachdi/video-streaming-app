package com.medtech.videostreaming.maper;

import com.medtech.videostreaming.dto.VideoDTO;
import com.medtech.videostreaming.model.Video;

import java.util.Optional;

public class MapperClass {
    public static VideoDTO videoToVideoDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();

        // Mapping between the VideoFinded <-> VideoDTO
        videoDTO.setVideoStatus(video.getVideoStatus());
        videoDTO.setDescription(video.getDescription());
        videoDTO.setTitle(video.getTitle());
        videoDTO.setUrl(video.getUrl());
        videoDTO.setTags(video.getTags());
        videoDTO.setThumbnailUrl(video.getThumbnailUrl());
        videoDTO.setId(video.getId());
        videoDTO.setLikeCount(video.getLikes().get());
        videoDTO.setDislikeCount(video.getDisLikes().get());
        videoDTO.setVideoCount(video.getViewCount().get());

        return videoDTO;

    }
}
