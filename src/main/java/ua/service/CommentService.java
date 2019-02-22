package ua.service;

import ua.entity.Comment;
import ua.model.request.CommentRequest;

public interface CommentService {

    Integer saveComment(CommentRequest request);

    Comment findById(Integer id);

}
