package it.course.myblogc3.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReasonUpdateRequest {
				
		private Long advisoryId;
		private String advisoryReasonName;
	
		@NotBlank
		private   String advisoryReasonNameNew;
	
}
