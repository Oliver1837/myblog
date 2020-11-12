package it.course.myblogc3.payload.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponse {
	
	private Long id;
	
	private String title;
	
	private String content;

	private Long authorId;
	
	private String authorName;
	
	private Date updateAt;
	
	private int commentsNr;
	

	public PostResponse(Long id, String title, String content, Long authorId, String authorName, Date updateAt) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.authorName = authorName;
		this.updateAt = updateAt;
		
	}




	

}
