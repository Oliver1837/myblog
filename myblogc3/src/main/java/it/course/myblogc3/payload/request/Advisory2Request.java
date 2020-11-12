package it.course.myblogc3.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Advisory2Request {
	@NotNull
	private	Long idComment;
	@NotNull
	private Long idReport;
	@NotNull
	private Long idAdvisoryReason;
	
	@NotBlank  @NotEmpty @Size(max=26)
	private String status;
}
