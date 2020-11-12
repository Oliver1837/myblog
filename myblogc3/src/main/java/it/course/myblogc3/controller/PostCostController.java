package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.PostCost;
import it.course.myblogc3.entity.PostCostId;
import it.course.myblogc3.payload.request.PostCostRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.PostCostRepository;
import it.course.myblogc3.repository.PostRepository;

@RestController
public class PostCostController {
		
	
	@Autowired PostCostRepository postCostRepository;
	@Autowired PostRepository postRepository; 
	
	@PostMapping("private/create-post-cost")
	@PreAuthorize("hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> createPostCost(@RequestBody PostCostRequest postCostRequest,
			HttpServletRequest request) {
		Optional<Post> p = postRepository.findByIdAndVisible(postCostRequest.getIdPost());
		if(!p.isPresent()) {
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post not found!", request.getRequestURI()),
					HttpStatus.OK);
		}
		PostCostId pci= new PostCostId(p.get(),postCostRequest.getStartDate(),postCostRequest.getEndDate());
		boolean exists = postCostRepository.existsById(pci);
		if(exists) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post cost already exists!", request.getRequestURI()),
					HttpStatus.OK);
		}
		PostCost pc = new PostCost(pci, postCostRequest.getShiftCost()) ;
		postCostRepository.save(pc);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "New post add", request.getRequestURI()),
				HttpStatus.OK);
	}
	
}
