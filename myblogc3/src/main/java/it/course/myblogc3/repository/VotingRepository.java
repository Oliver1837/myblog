package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.Voting;
import it.course.myblogc3.entity.VotingId;

@Repository
public interface VotingRepository extends JpaRepository<Voting, VotingId>{
				
	
	List<Voting> findByVotingIdContains(Long id);
	
	//select count(p.id) , AVG(r.vote) from  post as  p join rating as r on r.post_id = p.id where p.is_visible=true ;
	
	
	/*
	 * @Query(
	 * value="select new com.example.myblog.payload.response.PostResponseWithVoting"
	 * + "(" + "p.id, " + "p.title, " + "p.content, " + "p.author.username," +
	 * "p.updateAt ," + "p.dbfile.fileName ," + "count(v.vote) ," +
	 * "coalesce(avg(v.vote), 0.00)"
	 * 
	 * + ")" + "from  Voting  v  " + "right outer join " + " Post p " +
	 * "on p.id = v.votingId.post.id " + "where p.visible=true " + "group by (p.id)"
	 * + "order by p.updateAt desc") List<PostResponseWithVoting>
	 * findPostCoutAndAvg();
	 * 
	 * @Query(
	 * value="select new com.example.myblog.payload.response.PostResponseWithVoting"
	 * + "(" + "p.id, " + "p.title, " + "p.content, " + "p.author.username," +
	 * "p.updateAt ," + "p.dbfile.fileName ," + "count(v.vote) ," +
	 * "coalesce(avg(v.vote), 0.00)"
	 * 
	 * + ")" + "from  Voting  v  " + "right outer join " + " Post p " +
	 * "on p.id = v.votingId.post.id " + "where p.visible=true  and " +
	 * " p.author.id=:id " + "group by (p.id)" + "order by p.updateAt desc")
	 * List<PostResponseWithVoting> findPostCoutAndAvgById(@Param("id") Long id);
	 */
	/*
		 * Query(value="select new com.example.myblog.payload.response.AuthorResponse" +
		 * "(" + "p.author.id, " + "p.author.username," + "p.author.email ," +
		 * "count(v.vote) ," + "coalesce(avg(v.vote), 0.00)" + ")" + "from  Voting  v  "
		 * + "right outer join " + " Post p " + "on p.id = v.votingId.post.id " +
		 * "where p.visible=true and" + " p.author.id=:id " + "group by (p.id) " +
		 * "order by p.updateAt desc") List<AuthorResponse>
		 * findPostCoutAndAvgByIdUser(@Param("id") Long id);
		 */
	/*
	 * @Query(value="select new com.example.myblog.payload.response.AuthorResponse"
	 * + "(" + "p.author.id, " + "p.author.username, " + "p.author.email, " +
	 * "COUNT(v.vote), " + "COALESCE(AVG(v.vote), 0.00) AS average" + ")" +
	 * "FROM Voting v " + "RIGHT OUTER JOIN " + "Post p " +
	 * "ON p.id = v.votingId.post.id " +
	 * "WHERE p.visible=true AND p.author.id=:authorId " + "" ) AuthorResponse
	 * findAuthorCountAndAvg(@Param("authorId") Long authorId);
	 * 
	 * @Query(value="select new com.example.myblog.payload.response.AuthorReport" +
	 * "(" + "p.author.id, " + "p.author.username, " + "COUNT(p.id), " +
	 * "COALESCE(AVG(v.vote), 0.00) AS average" + ")" + "FROM Voting v " +
	 * "RIGHT OUTER JOIN " + "Post p " + "ON p.id = v.votingId.post.id " +
	 * "WHERE p.visible=true" + " group by (p.author.id)" ) List<AuthorReport>
	 * findAuthorCountAndAvgAll();
	 * 
	 * @Query(value="select new com.example.myblog.payload.response.PostReport" +
	 * "(" + "p.id, " + "p.title, " + "COALESCE(AVG(v.vote), 0.00) AS average," +
	 * "p.visible," + "p.author.username" + ")" + "FROM Voting v " +
	 * "RIGHT OUTER JOIN " + "Post p " + "ON p.id = v.votingId.post.id " + "" +
	 * " group by (p.id)" ) List<PostReport> findPostAndAvgAll();
	 * 
	 * 
	 */
	
	
	
	
	@Query(value="SELECT AVG(VOTE) FROM VOTING WHERE POST_ID =:id",nativeQuery=true)
	Double findAvgByIdPost(@Param("id") long id);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO VOTING(vote,voter_id,post_id,created_at)"
			+ "	VALUES(:vote,:voter,:postId,CURRENT_TIMESTAMP)",nativeQuery=true)
	int insertVote(@Param("vote") int vote, @Param("voter") long voter, @Param("postId") long postId);
	@Transactional
	@Modifying
	@Query(value="UPDATE  VOTING"
			+ "	SET  vote = :vote	"
			+ "	WHERE post_id=:postId AND voter_id=:voter",nativeQuery=true)
	int updateVote(@Param("vote") int vote, @Param("voter") long voter, @Param("postId") long postId);
	
	 
	
	
	
}
