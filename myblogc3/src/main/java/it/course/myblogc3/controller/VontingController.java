package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.entity.Voting;
import it.course.myblogc3.entity.VotingId;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.VotingRepository;
import it.course.myblogc3.service.UserService;

@RestController
@Validated
public class VontingController {
		
	@Autowired
	PostRepository	postRepository;
	@Autowired
	TagRepository	tagRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	VotingRepository votingRepository;
	
	@Autowired
	UserService	userService;
	@PostMapping("private/vote-post")
	@PreAuthorize("hasRole('READER')")
	public ResponseEntity<ApiResponseCustom> votePost(@RequestParam Long postId, @RequestParam @Valid @Min(1) @Max(5) int vote,
			HttpServletRequest request) {

		Long pid = postRepository.getPostId(postId);
		if(pid==null)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "OK", "Post not found", request.getRequestURI()), HttpStatus.NOT_FOUND);

		User u = userService.getAuthenticatedUser();
				
		Optional<Voting> sv = votingRepository.findById(new VotingId(new Post(pid), new User(u.getId())));
		String msg=" ";
		if(sv.isPresent()) {
			votingRepository.updateVote(vote, u.getId(), pid);
			msg="Vote update to " + vote;

		}else {
			 votingRepository.insertVote(vote, u.getId(), pid);
			 msg="New vote added " + vote + " to post " + postId;
		}
			
		
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
				Instant.now(),201,"OK", "New voting '"+sv.toString()+"' has been created" ,request.getRequestURI() ),HttpStatus.OK);

	}
	
	
	

}
