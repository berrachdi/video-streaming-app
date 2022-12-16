package com.medtech.videostreaming.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    public String uploadVideo(MultipartFile file);
}
