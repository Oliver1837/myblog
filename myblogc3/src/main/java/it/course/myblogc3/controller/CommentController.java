package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.CommentRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.payload.response.CommentResponse;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.service.UserService;

@RestController
public class CommentController {
		
		@Autowired
		CommentRepository commentRepository;
		@Autowired
		UserService userService;
		@Autowired
		PostRepository	postRepository;
	
	@PostMapping("public/create-comment/{postId}")
	@PreAuthorize("hasRole('EDITOR') or hasRole('READER')")

	public ResponseEntity<ApiResponseCustom> createComment(@PathVariable long postId,@RequestBody CommentRequest commentRequest, HttpServletRequest request) {
		
		User loggedUser = userService.getAuthenticatedUser();
		boolean post= postRepository.existsByIdAndVisibleTrue(postId);
		
		if(post) {

			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"OK",
							"Post not found"
							, request.getRequestURI()
						), HttpStatus.OK);
		}
		
		
		
		
		Comment c = new Comment(commentRequest.getComment(),new Post(postId),loggedUser);
		
		commentRepository.save(c);
		
		//postRepository.getOne(postId).getComments().add(c);
		
		 return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK",
						"Comment add"
						, request.getRequestURI()
					), HttpStatus.OK);
	}
	

	
	@GetMapping("public/get-comment/{commentId}")
	public ResponseEntity<ApiResponseCustom> 	getComment(
			@PathVariable long commentId,
			HttpServletRequest request){
		
		
		Optional<CommentResponse> comment = commentRepository.getCommentVisible(commentId);
		if(!comment.isPresent()) {
			

			return  new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"OK",
							"Comment not found"
							, request.getRequestURI()
						), HttpStatus.OK);
			
		}
		
		
		
		
		return  new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK",
						comment.get()
						, request.getRequestURI()
					), HttpStatus.OK);
	}
	
	
	

	@DeleteMapping("private/delete-comment/{commentId}")
	public ResponseEntity<ApiResponseCustom> deleteComment(@PathVariable long commentId,
			HttpServletRequest request){
		
		 int cancellazione=  commentRepository.deleteComment(commentId);
		
		
			
		
		
		return  new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",
					"Comment with "+commentId+" has been removef from database" + cancellazione
					, request.getRequestURI()
				), HttpStatus.OK);
	}
}
