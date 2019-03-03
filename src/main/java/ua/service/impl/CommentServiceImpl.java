package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.entity.Comment;
import ua.entity.User;
import ua.exception.CafeException;
import ua.model.request.CommentRequest;
import ua.repository.CommentRepository;
import ua.service.CommentService;
import ua.service.UserService;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository repository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public String saveComment(CommentRequest commentRequest) {
        LOG.info("In 'saveComment' method");
        User user = userService.findCurrentUser();
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setUser(user);
        repository.save(comment);
        LOG.info("Exit from 'saveComment' method");
        return comment.getId();
    }

    @Override
    public Comment findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Comment with id [%s} not found", id)));
    }

}
