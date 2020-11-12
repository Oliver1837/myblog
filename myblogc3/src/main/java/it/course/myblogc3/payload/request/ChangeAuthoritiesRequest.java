package it.course.myblogc3.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.course.myblogc3.entity.AuthorityName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class ChangeAuthoritiesRequest {
	@Size(max=12 , min= 3,message ="Username length min 3 and max 12 chars")//Personalizzare il messaggio di errore con il parametro message=""
	@NotBlank(message="User must not be blank")//Personalizzare il messaggio di errore con il parametro message=""
	private String username;
	
	
	
	private Set<AuthorityName> authorityName;
	
	
	
}
