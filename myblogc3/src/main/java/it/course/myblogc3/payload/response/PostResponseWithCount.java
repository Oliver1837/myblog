package it.course.myblogc3.payload.response;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data  @NoArgsConstructor
public class PostResponseWithCount {
	
	public PostResponseWithCount(Long id, String title, Long authorId, String authorName, Date updateAt, long commentsNr) {
		super();
		this.id = id;
		this.title = title;
		this.authorId = authorId;
		this.authorName = authorName;
		this.updateAt = updateAt;
		this.commentsNr = commentsNr;
	}

	private Long id;
	
	private String title;

	private Long authorId;
	
	private String authorName;
	
	private Date updateAt;
	
	private long commentsNr;


}
