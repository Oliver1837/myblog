package it.course.myblogc3.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class PostCostId implements Serializable{
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POST_ID" ,nullable=false)
	private Post post;
	@Column(columnDefinition = "START_DATE",nullable = false)
	private LocalDate startDate;
	@Column(columnDefinition = "END_DATE",nullable = false)
	private LocalDate endDate;
	
	
}
