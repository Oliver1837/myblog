package it.course.myblogc3.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor 
@EqualsAndHashCode(callSuper = false)
public class SignUpRequest {
	@Email
	@Size(max=120, min= 6)
	@NotBlank
	private String email;
	
	@Size(max=12 , min= 3)
	@NotBlank
	private String username;
	
	@Size(max=15, min= 5)
	@NotBlank
	private String password;
	
	
	

}
