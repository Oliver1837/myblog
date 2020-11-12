package it.course.myblogc3.payload.request;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReasonRequest {

		@NotNull @NotBlank @Size(max=15)
		private String advisoryReasonName;
		
		@NotNull
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
		private Date startDate;
	
		
		@NotNull @NotBlank @Size(max=15)
		private String severityDescription;
		
		
}
