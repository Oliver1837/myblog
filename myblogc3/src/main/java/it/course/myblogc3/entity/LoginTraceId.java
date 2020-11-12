package it.course.myblogc3.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginTraceId implements Serializable {

	private static final long serialVersionUID = 1L;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REPORTER", nullable=false)
	private User user;
	@Column(columnDefinition = "DATE",nullable = false)
	private LocalDate loginDate;
	public LoginTraceId(User user) {
		super();
		this.user = user;
	}
	
	
	
	
}
