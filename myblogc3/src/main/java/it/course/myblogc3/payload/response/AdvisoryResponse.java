package it.course.myblogc3.payload.response;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import it.course.myblogc3.entity.AdvisoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor

public class AdvisoryResponse {
	private Date createdAt;
	private Date updatedAt;
	private String severityDescription;
	private int severityValue;
	private long advisoryReasonId;
	private String advisoryReasonName;
	private String status;
	private String reporter; // reporter (username)
	private String reported; // comment author (username)
	private long commentId;

	public AdvisoryResponse(Date createdAt, Date updatedAt, String severityDescription, int severityValue,
			long advisoryReasonId, String advisoryReasonName, AdvisoryStatus status, String reporter, String reported,
			long commentId) {
		super();
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.severityDescription = severityDescription;
		this.severityValue = severityValue;
		this.advisoryReasonId = advisoryReasonId;
		this.advisoryReasonName = advisoryReasonName;
		this.status = status.toString();
		this.reporter = reporter;
		this.reported = reported;
		this.commentId = commentId;
	}

	
}
