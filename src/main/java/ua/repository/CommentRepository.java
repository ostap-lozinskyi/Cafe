package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import ua.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String>, JpaSpecificationExecutor<Comment> {
		
	@Query("SELECT c FROM Comment c WHERE c.id=?1")
	Comment findOneCommentRequest(String id);
	
	Optional<Comment> findCommentById(String id);
}
