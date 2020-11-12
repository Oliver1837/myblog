package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.response.CommentResponse;
import it.course.myblogc3.payload.response.CommentResponseForPostDerail;

@Repository
public interface CommentRepository extends JpaRepository<Comment , Long>{
	
	
	
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.CommentResponse("
			+ " c.id,"
			+ "	c.createdAt,"
			+ "	c.comment,"
			+ "	c.visible,"
			+ "	c.commentAuthor.username,"
			+ "	c.post.title"
		
			+ ")"
			+ "FROM Comment c "
			
			+ ""
			+ "WHERE c.visible = true and c.id=:x")
	Optional<CommentResponse> getCommentVisible(@Param("x") long id);
	@Query(value ="SELECT " 
			+ " c.commentAuthor "
		
			+ ""
			+ "FROM Comment c "

			+ ""
			+ "WHERE  c.id=:x ")
	User getIdUserFromComment(@Param("x") Long id);//PRENDO L?AUTOR COMMENT DALL' ID DEL COMMENTO
	
	@Query(value="SELECT c.commentAuthor FROM Comment c "
			+ "WHERE c.id = :commentId ")
	Optional<User> getCommentAuthorByCommentId(long commentId);
	
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.CommentResponseForPostDerail("
			+ " c.id,"
			+ "	c.createdAt,"
			+ "	c.comment,"
			+ "	c.commentAuthor.username,"
			+ "	c.post.title"
			+ ")"
			+ "FROM Comment c "
			
			+ ""
			+ "WHERE c.visible = true and c.post.id=:x")
	List<CommentResponseForPostDerail> getCommentsVisibleByPostId(@Param("x") long id);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE  FROM Comment c WHERE c.id = :id ")
	int deleteComment(@Param("id") long id);//REturn type int/I
	Optional<Comment> findByIdAndVisibleTrue(Long idComment);
	
	@Query(value ="SELECT new it.course.myblogc3.entity.Comment("
			+ " c.id,"
			+ "	c.createdAt,"
			+ "	c.comment,"
			+ "	c.visible,"
			+ "	a,"
			+ "	p"
			+ ")"
			+ "FROM Comment c "
			+ "	LEFT JOIN c.post p "
			+ "	LEFT JOIN c.commentAuthor a "
			
			+ ""
			+ "WHERE c.visible = true and c.id=:x")
	Optional<Comment> getCommentVisibleTrue(@Param("x") long id);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE  FROM Comment c SET c.visible = false WHERE c.id = :id ")
	int updateVisibleComment(@Param("id") long id);//REturn type int/I

	
	
	
}
