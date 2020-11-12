package it.course.myblogc3.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="LOGIN_ATTEMPT")
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginAttempt {
	
	@EmbeddedId
	private LoginAttemptId loginAttemptId;
	@Column(name="LOGIN_FAIL_AT",
			updatable=false, insertable=false ,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private LocalDateTime loginFailAt;
	@Column(name="CONUNTER",nullable=false ,columnDefinition = "TINYINT(1)")
	private int counter ;
	@Column(name="IP_ADDRESS",nullable= false , length = 40)
	private String ip;
	
	public LoginAttempt(LoginAttemptId loginAttemptId, int counter, String ip) {
		super();
		this.loginAttemptId = loginAttemptId;
		this.counter = counter;
		this.ip = ip;
	}

	
}
