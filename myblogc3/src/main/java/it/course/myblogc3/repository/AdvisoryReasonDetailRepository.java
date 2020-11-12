package it.course.myblogc3.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisoryReasonDetail;
import it.course.myblogc3.entity.AdvisoryReasonDetailId;
import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.payload.response.AdvisoryReasonResponse;

@Repository
public interface AdvisoryReasonDetailRepository extends JpaRepository<AdvisoryReasonDetail, AdvisoryReasonDetailId>{
							
	
	  Optional<AdvisoryReasonDetail>
	  findByAdvisoryReasonDetailId(AdvisoryReasonDetailId advisoryReasonDetailId);
	  Optional<AdvisoryReasonDetail>
	  findByAdvisoryReasonDetailIdAdvisoryReasonAndEndDateEquals(AdvisoryReason
	  advisoryReason,Date date);
	  
	  
	  
		
		  
	  @Query("SELECT new it.course.myblogc3.payload.response.AdvisoryReasonResponse("
	  + "ar.advisoryReasonDetailId.advisoryReason.advisoryReasonName," +
	  "	ar.advisorySeverity.levelDescription," 
	  + "ar.advisorySeverity.severityValue)" +
	  "FROM  AdvisoryReasonDetail ar"
	  + "" 
	  + "	WHERE ar.endDate =:date" +
	  "	ORDER BY ar.advisorySeverity.severityValue, ar.advisorySeverity.levelDescription desc") 
	  List<AdvisoryReasonResponse> findAllByEndDateIsNullAndOrderByLevelGravity(@Param("date")Date endDate);
		  
		  @Query("SELECT new it.course.myblogc3.entity.AdvisoryReason("
				  + "ar.advisoryReasonDetailId.advisoryReason.id," +
				  "	ar.advisoryReasonDetailId.advisoryReason.advisoryReasonName" 
				  +" )" +
				  "FROM  AdvisoryReasonDetail ar	"
				  + ""
				  + "" 
				  + "WHERE (ar.endDate ='9999-12-31' or   ar.endDate<= CURRENT_TIMESTAMP )"
				  + " and ar.advisoryReasonDetailId.advisoryReason.advisoryReasonName = :desc  and ar.advisoryReasonDetailId.startDate>= CURRENT_TIMESTAMP"
				  + "	"
				  + "	" )
				 
		  Optional<AdvisoryReason> findByEndDateIsNullOrCurrentTimeAndDescAndStartDate(@Param("desc")String desc);
		 
		  @Query(value="SELECT ard.advisorySeverity.severityValue "
					+ "FROM Advisory a "
					+ "LEFT JOIN AdvisoryReasonDetail ard "
					+ "ON a.advisoryId.advisoryReason.id = ard.advisoryReasonDetailId.advisoryReason.id "
					+ "WHERE ard.advisoryReasonDetailId.advisoryReason.id = :reasonId "
					+ "AND a.createdAt BETWEEN ard.advisoryReasonDetailId.startDate AND ard.endDate ")
			int getSeverityValue(@Param ("reasonId") long reasonId);
			
}
