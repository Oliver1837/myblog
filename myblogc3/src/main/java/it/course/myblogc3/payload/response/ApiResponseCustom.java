package it.course.myblogc3.payload.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class ApiResponseCustom {

	private Instant timestamp;//Java 8 e fa parte di java time
	
	private int status;
	
	private String error;
	
	private Object message;
	
	private String path;
	
}
