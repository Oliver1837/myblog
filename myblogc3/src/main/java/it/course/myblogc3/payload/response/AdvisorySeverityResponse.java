package it.course.myblogc3.payload.response;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter @AllArgsConstructor @NoArgsConstructor
public class AdvisorySeverityResponse {

		private String levelDescription;
	
		private int severityValue;
	
}
