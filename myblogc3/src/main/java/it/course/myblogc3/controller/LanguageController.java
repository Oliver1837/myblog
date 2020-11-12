package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Language;
import it.course.myblogc3.payload.request.LanguageRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.LanguageRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.TagRepository;

@RestController
@Validated
public class LanguageController {
	@Autowired
	PostRepository	postRepository;
	@Autowired
	TagRepository	tagRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	LanguageRepository languageRepository;
		@PostMapping("private/create-lang")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponseCustom> createLang(@RequestBody LanguageRequest languageRequest,HttpServletRequest request){
			
			if(languageRepository.existsByLangCodeOrLangDesc(languageRequest.getLangCode().toUpperCase(), languageRequest.getLangDesc().toUpperCase())) {
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(Instant.now(), 404,	"OK",
							"Language already presents", request.getRequestURI()), HttpStatus.NOT_FOUND);
				
			}
			Language l = new Language(languageRequest.getLangCode().toUpperCase() ,languageRequest.getLangDesc().toUpperCase());

			languageRepository.save(l);
			
			
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"OK",
						"Language is create"
						, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
			
			
		}
		@PutMapping("private/update-lang")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponseCustom> updateLang(@Valid @RequestBody LanguageRequest languageRequest,HttpServletRequest request){
			
			
			Optional<Language> l = languageRepository.findById(languageRequest.getLangCode());
			if(l.isPresent()) {
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"OK",
							"Language not found "
							, request.getRequestURI()
						), HttpStatus.NOT_FOUND);
				
			}
			
			languageRepository.save(l.get());
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"OK",
						"Language is update"
						, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
			
			
		}
		
		@PutMapping("private/update-visible-language/{langCode}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponseCustom> updateVisibleLang(@PathVariable @NotBlank  @Size(min=2,max=2) String langCode,HttpServletRequest request){
			
			
			Optional<Language> l = languageRepository.findById(langCode);
			if(!l.isPresent()) {
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 404,	"OK",
							"Language not found "
							, request.getRequestURI()
						), HttpStatus.NOT_FOUND);
				
			}
			l.get().setVisible(!l.get().getVisible());
			languageRepository.save(l.get());
			
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404,	"OK",
						"Language visible  is update"
						, request.getRequestURI()
					), HttpStatus.NOT_FOUND);
			
			
		}
		
		
}
