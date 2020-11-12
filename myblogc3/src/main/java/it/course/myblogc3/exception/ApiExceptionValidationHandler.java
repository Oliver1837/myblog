package it.course.myblogc3.exception;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.course.myblogc3.payload.response.ApiResponseCustom;
@ControllerAdvice // Scatena l'esecuzione della classe intercetta gli expection
public class ApiExceptionValidationHandler extends ResponseEntityExceptionHandler{
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request
			){
		
		BindingResult bindingResult = ex.getBindingResult();//I valori vengono bindati  e si registrano gli errori
		List<ApiFieldError> apiFieldError =  bindingResult.getFieldErrors()// prendo gli errori come lista
				.stream()//li trasformo in in stream 
				.map(fieldError->new ApiFieldError( // faccio la mappattura creando una nuova lista ApiFieldError cioè prendi solo gli errori che sono uguali ad ApiFieldError
				fieldError.getField(),
				fieldError.getCode(),
				fieldError.getDefaultMessage(),
				fieldError.getRejectedValue()
				
				)).collect(Collectors.toList());
		
		return new ResponseEntity<Object>(new ApiResponseCustom(
				Instant.now(),
				400,
				"Bad Request",
				apiFieldError,
				request.getDescription(false).replace("uri", "")
				)
				,HttpStatus.UNPROCESSABLE_ENTITY);
	    }

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(
			ConstraintViolationException e,
			WebRequest request) {
	
		Set<ConstraintViolation<?>> ex= e.getConstraintViolations();

		List<ApiFieldError> apiFieldError =
			ex.stream()
			.map(fieldError->new ApiFieldError(
					fieldError.getPropertyPath().toString(),
					"Constraint violation",
					fieldError.getMessage(),
					fieldError.getInvalidValue()))
			.collect(Collectors.toList());

		return new ResponseEntity<Object>(new ApiResponseCustom(
			Instant.now(), 422, "Bad Request", apiFieldError,request.getDescription(false).replace("uri=", "")
			), HttpStatus.UNPROCESSABLE_ENTITY
		);
	}
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e,
			WebRequest request) {
		ApiFieldError apiFieldError =
				new ApiFieldError(
						e.getName(),
						"MismatchException",
						e.getMessage(),
						request.getParameter(e.getName()));
	
		return new ResponseEntity<Object>(new ApiResponseCustom(
			Instant.now(), 422, "Bad Request", apiFieldError,request.getDescription(false).replace("uri=", "")
			), HttpStatus.UNPROCESSABLE_ENTITY
		);
	}
	/*
	 * @ExceptionHandler(Exception.class) protected ResponseEntity<Object>
	 * handleGenericException( Exception e, WebRequest request) { ApiFieldError
	 * apiFieldError = new ApiFieldError( "", "Generic Exception", e.getMessage(),
	 * "");
	 * 
	 * return new ResponseEntity<Object>(new ApiResponseCustom( Instant.now(), 422,
	 * "Bad Request", apiFieldError,request.getDescription(false).replace("uri=",
	 * "") ), HttpStatus.UNPROCESSABLE_ENTITY ); }
	 */
}
