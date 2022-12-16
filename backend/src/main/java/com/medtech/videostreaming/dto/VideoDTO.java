package com.medtech.videostreaming.dto;

import com.medtech.videostreaming.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {

    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String url;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer videoCount;

}
