package it.course.myblogc3.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiFieldError {//Ci serve a catturare  errore e far capire di che errpre si tratta
		
	private String field;//campo dove si verifica l'errore
	
	private String error;//Ci dice in che errore siamo 
	
	private String defaultMessage;
	
	private Object rejecterdValue;//Valore rifiutato 
	
	
}
