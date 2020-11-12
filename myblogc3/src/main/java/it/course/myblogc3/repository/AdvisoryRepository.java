package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Advisory;
import it.course.myblogc3.entity.AdvisoryId;
import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.payload.response.AdvisoryResponse;
@Repository
public interface AdvisoryRepository extends JpaRepository<Advisory,AdvisoryId >{
	
	//	Optional
	boolean existsByAdvisoryIdAndStatusFalse(AdvisoryId id);
	
			@Query("select a from Advisory a where a.advisoryId.comment.id=:commentId "
			+ "And a.advisoryId.reporter.id=:reporterId "
			+ "And a.advisoryId.advisoryReason.id=:reason")
			Optional<Advisory> getAdvisory(@Param("commentId") long commentId,@Param("reporterId") long reporterId,@Param("reason") long reason);
			@Query(value="SELECT * FROM advisory WHERE reporter=:reporterId "
					+ "AND comment_id=:commentId AND advisory_reason_id=:reasonId", nativeQuery=true)
			Advisory getAdvisoryById(@Param("reporterId") long reporterId,@Param("commentId") long commentId, @Param("reasonId") long reasonId);
			
			
			
			@Query("select new it.course.myblogc3.payload.response.AdvisoryResponse("
					+ "a.createdAt,"
					+ "a.updateAt,"
					+ "ad.advisorySeverity.levelDescription,"
					+ "ad.advisorySeverity.severityValue,"
					+ "ad.advisoryReasonDetailId.advisoryReason.id,"
					+ "ad.advisoryReasonDetailId.advisoryReason.advisoryReasonName,"
					+ "a.status,"
					+ "a.advisoryId.reporter.username,"
					+ "a.advisoryId.comment.commentAuthor.username,"
					+ "a.advisoryId.comment.id"
					+ ")"
					+ " from Advisory a "
					+ "	left join AdvisoryReasonDetail  ad on "
					+ "	 a.advisoryId.advisoryReason.id = ad.advisoryReasonDetailId.advisoryReason.id"
					+ "  where a.status IN(0,1)  AND a.createdAt BETWEEN ad.advisoryReasonDetailId.startDate AND ad.endDate "
					+ "  "
					+ ""
					+ " ORDER BY a.status ASC")
			List<AdvisoryResponse> getAdvisoryOpenAndInProgress();
			
			boolean existsByAdvisoryIdCommentAndAdvisoryIdAdvisoryReason(Comment c ,AdvisoryReason ar);
			
		//	boolean existsByAdvisoryIdCommentAndAdvisoryIdAdvisoryReason(a.getAdvisoryId().getComment(), a.getAdvisoryId().getAdvisoryReason());
			
			
}
