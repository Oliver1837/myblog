package it.course.myblogc3.payload.request;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter @AllArgsConstructor @NoArgsConstructor
public class AdvisorySeverityRequest {

		@NotBlank
		@NotNull @NotBlank @Size(max=15)
		private String levelDescription;
		@Digits(fraction = 0 ,integer = 3)
		private int severityValue;
	
}
