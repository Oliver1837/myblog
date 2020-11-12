package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisoryReasonDetail;
import it.course.myblogc3.entity.AdvisoryReasonDetailId;
import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.payload.request.AdvisoryReasonRequest;
import it.course.myblogc3.payload.request.AdvisoryReasonUpdateRequest;
import it.course.myblogc3.payload.request.AdvisoryRequest;
import it.course.myblogc3.payload.request.AdvisorySeverityRequest;
import it.course.myblogc3.payload.response.AdvisoryReasonResponse;
import it.course.myblogc3.payload.response.AdvisorySeverityResponse;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc3.repository.AdvisoryReasonRepository;
import it.course.myblogc3.repository.AdvisoryRepository;
import it.course.myblogc3.repository.AdvisorySeverityRepository;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.service.UserService;

@RestController
public class AdvisorySeverityController {


	 
	 @Autowired AdvisorySeverityRepository advisoryReasonRepository;
	 
	 //post
	 @PostMapping("private/create-advisory-severity")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<ApiResponseCustom> createAdvisorySeverity(@Valid @RequestBody 
			 AdvisorySeverityRequest advisorySeverityRequest
			 , HttpServletRequest request){
		 if(advisoryReasonRepository.existsById(advisorySeverityRequest.getLevelDescription())) {
			 return new ResponseEntity<ApiResponseCustom>( new
					  ApiResponseCustom(Instant.now(),200,"OK","severity already exists",request.
					  getRequestURI() ), HttpStatus.OK);
			 
		 }
		 
		 advisoryReasonRepository.save(new AdvisorySeverity(advisorySeverityRequest.getLevelDescription(),advisorySeverityRequest.getSeverityValue()));
	 
		 return new ResponseEntity<ApiResponseCustom>( new
				  ApiResponseCustom(Instant.now(),200,"OK","new severity added",request.
				  getRequestURI() ), HttpStatus.OK);
	 }
		 
		 //get order by severity value
	 @GetMapping("private/get-advisory-severity")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<ApiResponseCustom> getAdvisorySeverity
			( HttpServletRequest request){
		 List<AdvisorySeverity> ads = advisoryReasonRepository.findAllAndOrderBySeverityValue();
		 if(ads.isEmpty()) {
			 return new ResponseEntity<ApiResponseCustom>( new
					  ApiResponseCustom(Instant.now(),200,"OK","severities not found",request.
					  getRequestURI() ), HttpStatus.OK);
			 
		 }
		 
		 
		 
	
	 
		 return new ResponseEntity<ApiResponseCustom>( new
				  ApiResponseCustom(Instant.now(),200,"OK",ads,request.
				  getRequestURI() ), HttpStatus.OK);
	 }
}
