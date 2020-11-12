package it.course.myblogc3.payload.response;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponseWithPreferredTags {

	private Long id;
	private String title;
	private Long authorId;
	private String authorName;
	private Date updatedAt;
	private String[] tags;
	public PostResponseWithPreferredTags(Long id, String title, Long authorId, String authorName, Date updatedAt){

		this.id = id;
		this.title = title;
		this.authorId = authorId;
		this.authorName = authorName;
		this.updatedAt = updatedAt;

	}
	public static PostResponseWithPreferredTags createFromEntity(Post p) {
		return new PostResponseWithPreferredTags(
			p.getId(),
			p.getTitle(),
			p.getAuthor().getId(),
			p.getAuthor().getUsername(),
			p.getUpdateAt(),
			p.getTags().stream().map(t->t.getTagName()).toArray(String[]::new)
			);
		}

	
}