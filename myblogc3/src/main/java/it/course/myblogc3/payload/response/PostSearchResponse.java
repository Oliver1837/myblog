package it.course.myblogc3.payload.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class PostSearchResponse {
	
	private Long id;
	
	private String title;
	
	private String content;

	private Long authorId;
	
	private String authorName;
	
	private Date updateAt;
	
	





	

}
