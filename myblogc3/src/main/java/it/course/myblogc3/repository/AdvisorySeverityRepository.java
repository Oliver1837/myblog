package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.payload.response.AdvisorySeverityResponse;

@Repository
public interface AdvisorySeverityRepository extends JpaRepository<AdvisorySeverity, String>{
	
	@Query("SELECT  new it.course.myblogc3.entity.AdvisorySeverity"
			+ "( ads.levelDescription, ads.severityValue)"
			+ "	 FROM AdvisorySeverity ads "
			+ "  ORDER BY ads.severityValue DESC"
			+ ""
			)
	List<AdvisorySeverity> findAllAndOrderBySeverityValue();
	
	  @Query("SELECT  new it.course.myblogc3.entity.AdvisorySeverity"
				+ "(ars.levelDescription ,ars.severityValue)"
				+ "	 FROM AdvisorySeverity   ars "
				+ "	 LEFT JOIN  AdvisoryReasonDetail  ard on "
				+ "	 ard.advisorySeverity.levelDescription = ars.levelDescription "
				+ "	 WHERE 	ard.advisoryReasonDetailId.advisoryReason.id=:id"
				+ ""
				+ ""
				+ ""
				)
		Optional<AdvisorySeverity> findById(@Param("id") Long id);
	
	
	
	
}
