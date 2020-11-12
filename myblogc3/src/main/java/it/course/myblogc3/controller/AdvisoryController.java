package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Advisory;
import it.course.myblogc3.entity.AdvisoryId;
import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.entity.AdvisoryStatus;
import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.Advisory2Request;
import it.course.myblogc3.payload.request.AdvisoryRequest;
import it.course.myblogc3.payload.response.AdvisoryResponse;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc3.repository.AdvisoryReasonRepository;
import it.course.myblogc3.repository.AdvisoryRepository;
import it.course.myblogc3.repository.AdvisorySeverityRepository;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.BanService;
import it.course.myblogc3.service.UserService;

@RestController
public class AdvisoryController {

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AdvisoryRepository advisoryRepository;
	@Autowired
	AdvisorySeverityRepository advisorySeverityRepository;
	@Autowired
	AdvisoryReasonRepository advisoryReasonRepository;
	@Autowired
	AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
	@Autowired
	BanService banService;
	@PostMapping("public/create-advisory")
	@PreAuthorize("hasRole('EDITOR') or hasRole('READER')")
	public ResponseEntity<ApiResponseCustom> createAdvisory(@RequestBody AdvisoryRequest advisoryRequest, HttpServletRequest request) {
		
		
		  Optional<Comment> c =commentRepository.findByIdAndVisibleTrue(advisoryRequest.getCommentId());	  
		  if(!c.isPresent()) {
			  return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"COMMENT NOT FOUND"
							, request.getRequestURI()
						), HttpStatus.OK);
		  }
		  User loggedUser = userService.getAuthenticatedUser();
		  if(c.get().getCommentAuthor().getId() == loggedUser.getId()) {
			  return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 400,	"BAD REQUEST",
							"Not Advisory your comment"
							, request.getRequestURI()
						), HttpStatus.OK);
			  
		  }
		  
		  Optional<AdvisoryReason> ar = advisoryReasonDetailRepository.findByEndDateIsNullOrCurrentTimeAndDescAndStartDate(advisoryRequest.getReason());
		  if(!ar.isPresent()) {
			  return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"AdvisoryReason NOT FOUND"
							, request.getRequestURI()
						), HttpStatus.OK);
		  }
		  
		  
		  boolean exists = advisoryRepository.existsByAdvisoryIdCommentAndAdvisoryIdAdvisoryReason(c.get(), ar.get());
		  
		  AdvisoryId id = new AdvisoryId(c.get(), loggedUser, ar.get());
		  if(advisoryRepository.existsById(id) || exists) {
			  return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"ok",
							"Advisory already exists"
							, request.getRequestURI()
						), HttpStatus.OK);
		  }
		  Advisory ad = new Advisory(id,AdvisoryStatus.OPEN ,advisoryRequest.getDescription());
		  
		  advisoryRepository.save(ad);
		  
		  
		  
		  
			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"OK",
							"new advisory add"
							, request.getRequestURI()
						), HttpStatus.OK);
		}
		
	@PutMapping("private/change-status-advisory")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> changeUpdateAdvisory(
			@RequestBody Advisory2Request advisory2Request			,HttpServletRequest request) {
	
		
		Advisory a = advisoryRepository.getAdvisoryById(
				
					
						advisory2Request.getIdReport()
						,	advisory2Request.getIdComment()
						, advisory2Request.getIdAdvisoryReason()
				
				
				);
		if(a==null) {
			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"Advisory not found or closed"
							, request.getRequestURI()
						), HttpStatus.OK);
			
		}
		if(a.getStatus().ordinal() == 2 || a.getStatus().ordinal() == 3 ) {
			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"Advisory already closed"
							, request.getRequestURI()
						), HttpStatus.OK);
		}
		a.setStatus(AdvisoryStatus.valueOf(advisory2Request.getStatus()));
		
		if(advisory2Request.getStatus().equals(AdvisoryStatus.valueOf("CLOSE_WITH_CONSEQUENCE").toString())) {
			
			
			
			//recuperare la gravity
			int gravity = advisoryReasonDetailRepository.getSeverityValue(a.getAdvisoryId().getAdvisoryReason().getId());
			Optional<User> u = commentRepository.getCommentAuthorByCommentId(a.getAdvisoryId().getComment().getId());
			if(!u.isPresent()) {
				

				 return new ResponseEntity<ApiResponseCustom>(
							new ApiResponseCustom(
								Instant.now(), 200,	"OK",
								"Severity not present"
								, request.getRequestURI()
							), HttpStatus.OK);
				
			}
			//aggiornare banned Until
			
			
			banService.createBanUntil(gravity,u.get());
			
			userRepository.save(u.get());
			commentRepository.updateVisibleComment(advisory2Request.getIdComment());
		}
		
		
		
		advisoryRepository.save( a);
		  
		  
			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 200,	"OK",
							"new advisory add"
							, request.getRequestURI()
						), HttpStatus.OK);
		}
	@GetMapping("private/get-open-advisories")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getAdvisory(HttpServletRequest request) {
		
		List<AdvisoryResponse>  list = advisoryRepository.getAdvisoryOpenAndInProgress();
		if(list.isEmpty()) {

			 return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"NOT FOUND",
							"Advisory not found"
							, request.getRequestURI()
						), HttpStatus.OK);
		}
	
		 return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK",
						list
						, request.getRequestURI()
					), HttpStatus.OK);
		
	}
}
