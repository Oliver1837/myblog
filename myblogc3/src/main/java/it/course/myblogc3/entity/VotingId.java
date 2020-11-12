package it.course.myblogc3.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @AllArgsConstructor  @NoArgsConstructor
public class VotingId  implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="POST_ID" , nullable=false)
	private Post post;
	
	@ManyToOne
	@JoinColumn(name="VOTER_ID" , nullable=false)
	private User voter;
	public VotingId(Post post) {
		super();
		this.post = post;
	}
	
	@PrePersist
	public static void  prePersistVotingId(Post post) {
		VotingId votingId = new VotingId(post);
		
	}
	
	
	
	
}
