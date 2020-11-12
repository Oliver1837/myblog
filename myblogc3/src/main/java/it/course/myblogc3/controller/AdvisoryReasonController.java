package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Date;
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
import it.course.myblogc3.payload.response.AdvisoryReasonResponse;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc3.repository.AdvisoryReasonRepository;
import it.course.myblogc3.repository.AdvisoryRepository;
import it.course.myblogc3.repository.AdvisorySeverityRepository;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.service.AdvisoryService;
import it.course.myblogc3.service.UserService;

@RestController
public class AdvisoryReasonController {

	
	  @Autowired CommentRepository commentRepository;
	  
	  @Autowired UserService userService;
	  
	  @Autowired AdvisoryRepository advisoryRepository;
	  
	  @Autowired AdvisoryReasonRepository advisoryReasonRepository;
	  
	  @Autowired AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
	  
	  @Autowired AdvisorySeverityRepository advisorySeverityRepository;
	  
	  @Autowired AdvisoryService advisoryService;
	  
	  @PostMapping("private/create-advisory-reason")
	  @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponseCustom>
	  createAdvisoryReason(@Valid @RequestBody AdvisoryReasonRequest
	  advisoryReasonRequest, HttpServletRequest request){
	  
	  Optional<AdvisoryReason> ar =
	  advisoryReasonRepository.findByAdvisoryReasonName(advisoryReasonRequest.
	  getAdvisoryReasonName()); 
	  AdvisoryReason arNew = new AdvisoryReason();

	  Optional<AdvisorySeverity> as= advisorySeverityRepository.findById(  advisoryReasonRequest.getSeverityDescription());
	  if(!as.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>( new
					  ApiResponseCustom(Instant.now(),200,"OK","Advisory Severity  not found",request.
					  getRequestURI() ), HttpStatus.OK);
					  
		  }
	  if(ar.isPresent()) { 
	  Optional<AdvisoryReasonDetail> ard = advisoryReasonDetailRepository.findByAdvisoryReasonDetailIdAdvisoryReasonAndEndDateEquals(ar.get(),
			  advisoryService.fromCalendarToDate()
			  );
	 
	  AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail( new AdvisoryReasonDetailId (ar.get(),advisoryReasonRequest.getStartDate()),
			 
			  as.get());
	  
	  ardNew.setEndDate(advisoryReasonRequest.getStartDate());

	  
	  advisoryReasonDetailRepository.save(ardNew); 
	  } else {
	
	  arNew.setAdvisoryReasonName(advisoryReasonRequest.getAdvisoryReasonName());
	  advisoryReasonRepository.save(arNew); 

	  
	  
	  
	  AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail( new AdvisoryReasonDetailId (arNew,
	  advisoryReasonRequest.getStartDate()),
			  advisoryService.fromCalendarToDate()
			  ,
			  as.get()
	  );
	  advisoryReasonDetailRepository.save(ardNew); }
	  
	  
	  
	  return new ResponseEntity<ApiResponseCustom>( new
	  ApiResponseCustom(Instant.now(),200,"OK","new advisoryReason added",request.
	  getRequestURI() ), HttpStatus.OK);
	  
	  
	  }
	  
		/*
		 * @PutMapping("public/update-advisory-reason/{advisoryReasonId}")
		 * 
		 * @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponseCustom>
		 * updateAdvisoryReason(
		 * 
		 * @PathVariable Long advisoryReasonId ,
		 * 
		 * @RequestParam String newAdvisoryReasonName, HttpServletRequest request){
		 * 
		 * Optional<AdvisoryReason> arOld =
		 * advisoryReasonRepository.findById(advisoryReasonId);
		 * 
		 * 
		 * if(!arOld.isPresent()) { return new ResponseEntity<ApiResponseCustom>( new
		 * ApiResponseCustom(Instant.now(),200,"OK","advisoryReason not found",request.
		 * getRequestURI() ), HttpStatus.OK); } Optional<AdvisoryReason> arNew =
		 * advisoryReasonRepository.findByAdvisoryReasonName(newAdvisoryReasonName);
		 * 
		 * 
		 * if(arNew.isPresent()) { return new ResponseEntity<ApiResponseCustom>( new
		 * ApiResponseCustom(Instant.now(),200,"OK","advisoryReason  Name is present"
		 * ,request.getRequestURI() ), HttpStatus.OK); }
		 * arNew.get().setAdvisoryReasonName(newAdvisoryReasonName);
		 * advisoryReasonRepository.save(arNew.get());
		 * 
		 * return new ResponseEntity<ApiResponseCustom>( new
		 * ApiResponseCustom(Instant.now(),200,"OK","advisoryReason with id "
		 * +advisoryReasonId+" is update",request.getRequestURI() ), HttpStatus.OK);
		 * 
		 * 
		 * }
		 * 
		 * @GetMapping("private/get-all-advisory-reason")
		 * 
		 * @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponseCustom>
		 * getAllAdvisoryReason(HttpServletRequest request){
		 * 
		 * List<AdvisoryReasonResponse> listAr =
		 * advisoryReasonDetailRepository.findAllByEndDateIsNullAndOrderByLevelGravity()
		 * ;
		 * 
		 * 
		 * if(listAr.isEmpty()) { return new ResponseEntity<ApiResponseCustom>( new
		 * ApiResponseCustom(Instant.now(),200,"OK","Advisory not found",request.
		 * getRequestURI() ), HttpStatus.OK); }
		 * 
		 * 
		 * 
		 * return new ResponseEntity<ApiResponseCustom>( new
		 * ApiResponseCustom(Instant.now(),200,"OK",listAr,request.getRequestURI() ),
		 * HttpStatus.OK);
		 * 
		 * 
		 * }
		 */
	  	  @GetMapping("private/get-all-advisory-reason")
		  @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponseCustom>
		  getAllAdvisoryReason(HttpServletRequest request){
		 
		 List<AdvisoryReasonResponse> listAr =
		  advisoryReasonDetailRepository.findAllByEndDateIsNullAndOrderByLevelGravity(advisoryService.fromCalendarToDate())
		  ;
		 
		 
		 if(listAr.isEmpty()) { return new ResponseEntity<ApiResponseCustom>( new
		 ApiResponseCustom(Instant.now(),200,"OK","Advisory not found",request.
		 getRequestURI() ), HttpStatus.OK); }
		 
		 
		  
		  return new ResponseEntity<ApiResponseCustom>( new
		  ApiResponseCustom(Instant.now(),200,"OK",listAr,request.getRequestURI() ),
		  HttpStatus.OK);
		 
		  }
		 
		
}
