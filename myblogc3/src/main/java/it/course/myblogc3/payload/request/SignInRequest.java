package it.course.myblogc3.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor 
@EqualsAndHashCode(callSuper = false)
public class SignInRequest {
	
	@Size(max=120 , min= 3,message ="Username length min 3 and max 12 chars")//Personalizzare il messaggio di errore con il parametro message=""
	@NotBlank(message="User must not be blank")//Personalizzare il messaggio di errore con il parametro message=""
	private String usernameOrEmail;
	
	@Size(max=15, min= 5)
	@NotBlank
	private String password;
	
	

}
