package it.course.myblogc3.payload.response;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor

public class AdvisoryReasonResponse {
	private String advisoryReasonResponse;
	private String levelDescription;
	private int levelGravity;
	
}
