package it.course.myblogc3.payload.response;

import java.util.Date;

import it.course.myblogc3.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class CommentResponse {
	
	
	private Long id;
	private Date createdAt;
	private String comment;
	private Boolean visible;
	private String username;
	private String titolo;
	
	
	

}
