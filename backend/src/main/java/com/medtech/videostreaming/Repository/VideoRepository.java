package com.medtech.videostreaming.Repository;

import com.medtech.videostreaming.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {
}
