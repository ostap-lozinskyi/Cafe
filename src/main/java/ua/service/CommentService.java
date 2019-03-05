package ua.service;

import ua.model.entity.Comment;
import ua.model.request.CommentRequest;

public interface CommentService {

    String saveComment(CommentRequest request);

    Comment findById(String id);

}
