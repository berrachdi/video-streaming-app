package com.medtech.videostreaming.Repository;

import com.medtech.videostreaming.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
