package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.model.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

}
