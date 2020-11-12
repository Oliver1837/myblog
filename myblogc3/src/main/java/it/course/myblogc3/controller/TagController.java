package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.UserService;
@RestController
public class TagController {
		@Autowired
		TagRepository tagRepository;
		@Autowired 
		UserService userService;
		@Autowired
		UserRepository userRepository;
	
		@PutMapping("private/change-visibility")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponseCustom> changeVisibility(@RequestParam String tagName,HttpServletRequest request){
		
			Optional<Tag> tag =  tagRepository.findById(tagName) ;
			if(!tag.isPresent()) {
				
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"OK",
							"Tag not found"
							, request.getRequestURI()
						), HttpStatus.NOT_FOUND);
				
			}
			tag.get().setEnable(!tag.get().getEnable());
			
			tagRepository.save(tag.get());
			
			
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK",
						"Tag change status"
						, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
			
		}
		@PostMapping("private/add-preferred-tag")
		@PreAuthorize("hasRole('READER') or hasRole('EDITOR') ")
		@Transactional
		public ResponseEntity<ApiResponseCustom> addPreferredTag(@RequestParam String tagName,HttpServletRequest request){
			User u = userService.getAuthenticatedUser();
			
			Optional<Tag> t = tagRepository.findByTagNameAndEnableTrue(tagName);
			if(!t.isPresent()) {
				
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"Tag not found"
							, request.getRequestURI()
						), HttpStatus.NOT_FOUND);
				
			}
			Set<Tag> tags = u.getPreferredTags();
			if(tags.contains(t.get())) {
				

				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"OK",
							"Tag already present"
							, request.getRequestURI()
						), HttpStatus.NOT_FOUND);
			}
			
			
		
			u.getPreferredTags().add(t.get());
			userRepository.save(u);
			
			
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK",
						"Tag add to your preferred"
						, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
			
		}

}
