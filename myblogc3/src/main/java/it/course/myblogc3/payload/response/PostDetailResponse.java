package it.course.myblogc3.payload.response;

import java.util.Date;
import java.util.List;
import java.util.Set;

import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class PostDetailResponse {
	
	private long id;
	
	private String title;

	private String content;
	
	private long authorId;
	
	private String username;
	
	private Date updateAt;
	
	private double average; 
	
	private Set<String> tagNames;
	
	private List<CommentResponseForPostDerail> comment;

	public PostDetailResponse(long id, String title, String content, long authorId, String username, Date updateAt) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.username = username;
		this.updateAt = updateAt;
	}

	public PostDetailResponse(long id, String title, String content, long authorId, String username, Date updateAt,
			double average) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.username = username;
		this.updateAt = updateAt;
		this.average = average;
	}

	
	


	
	

}
