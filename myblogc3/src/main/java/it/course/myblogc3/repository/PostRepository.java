package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.payload.response.PostDetailResponse;
import it.course.myblogc3.payload.response.PostDetailTagResponse;
import it.course.myblogc3.payload.response.PostResponse;
import it.course.myblogc3.payload.response.PostResponseForSearch;
import it.course.myblogc3.payload.response.PostResponseWithPreferredTags;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByVisibleTrue();
	//2° metodo crearci la query 
	Post getOne(long id);
	boolean existsByIdAndVisibleTrue(long postId);
	boolean existsByTitleLike(String title);
	boolean existsByTitleStartsWith(String title);
	Optional<Post> findByIdAndVisible(Long id);
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt,"
			+ "size(p.comments)"
			+ ""
			+ ")"
			+ "FROM Post p	"
			+ ""
			+ "WHERE p.visible = 1 "
		
			+ "")
	List<PostResponse> getPostsVisible();
	

	

	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt "
		
			+ ")"
			+ "FROM Post p "
			+ " left join p.comments c "
			
			+ ""
			+ "WHERE p.visible = true and p.id=:x	"
			+ "GROUP BY c.id"
			+ "")
		PostResponse getPostVisibleAndCommentNr(@Param("x") long id);
	
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostDetailResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt,"
			+ "	AVG(v.vote)"
			+ ")"
			+ "FROM Post p "
			+ " left join Voting v on "
			+ "  p.id = v.votingId.post.id	"
			+ ""
			+ "WHERE p.visible = true and p.id=:x	"
			+ ""
			
			+ "")
	PostDetailResponse getPostVisible(@Param("x") long id);
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostDetailResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt"
			
			+ ")"
			+ "FROM Post p "
			
			
			+ ""
			+ "WHERE p.visible = true and p.id=:x")
	PostDetailResponse getPostVisibleWithComment(@Param("x") long id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE  FROM Post p WHERE p.id = :id ")
	int deletePost(@Param("id") long id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE  FROM Post p WHERE p.title LIKE CONCAT('%',:title,'%' )")
	int deletePostByTitle(@Param("title") String title);
	
	@Query("select post from Post post left join fetch post.author where post.id =:id")
	Post findOneWithAuthorById(@Param("id") Long id);
	//where room.id IN (:roomIDList)
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt"
			+"	"
			+ ")"
			+ "FROM Post p "
			+ "left join p.tags ts	"
			+ "WHERE p.visible = true and ts.enable = true	"
			+ "and	ts.tagName IN (:tags)	"
			+ "GROUP BY p.id")
	List<PostResponse> getPostsByTags(@Param("tags") Set<String> tags);
	
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt,"
			+ "	size(p.comments)"
			+"	"
			+ ")"
			+ "FROM Post p "
			+ ""
			+ "	"
			+ "WHERE p.visible = true and p.language.langCode=:langCode and p.language.visible= true"
			)
	List<PostResponse> getPostsByLangCode(@Param("langCode") String langCode);
	
	/*
	 * @Query(value ="SELECT new Post(" + " p.id," + "	p.author" +"	" + ")" +
	 * "FROM Post p " + "" + "" + "	" + "WHERE p.visible = true and p.id=:id" )
	 * Optional<Post> getPostByLangCode(@Param("id") Long id);
	 */
	@Query(value ="SELECT new it.course.myblogc3.payload.response.PostDetailResponse("
			+ " p.id,"
			+ "	p.title,"
			+ "	p.content,"
			+ "	p.author.id,"
			+ "	p.author.username,"
			+ "	p.updateAt,"
			+ "	AVG(v.vote)"
		
			+ ")"
			+ "FROM Post p "
			+ " left join Voting v on "
			+ "  p.id = v.votingId.post.id	"
			
			+ ""
			+ "WHERE p.visible = true and p.id=:x	"
			+ "	GROUP BY p.id"
			
			+ "")
	PostDetailResponse  getPostsVisibleById(@Param("x") Long postId);
	@Query(value ="SELECT "
			+ " p.id "
			+ "FROM Post p "
			+ "WHERE p.visible = true and p.id=:x"
					
			+ "")
	Long  getPostId(@Param("x") Long postId);
	

	boolean existsByTitle(String title);
		@Transactional
		@Modifying
	 @Query(value="UPDATE Post p	SET	 p.is_visible = CASE  WHEN is_visible = 1 THEN  0 ELSE  1 END	WHERE  p.id =:postId" , nativeQuery=true) 
	void updateStatusPost(@Param("postId")Long postId);
	 
	 @Transactional
	 @Modifying
	 @Query(value="UPDATE Post p SET p.visible = CASE  WHEN p.visible = 1 THEN  0 ELSE  1  END	WHERE  p.id =:postId" ) 
	void updateStatusPostJpql(@Param("postId")Long postId);
	 @Transactional
		@Modifying
		@Query(value="INSERT INTO POST(title,content,author)"
				+ "	VALUES(:title,:content,:author)",nativeQuery=true)
		int insertPost(@Param("title") String title, @Param("content") String content, @Param("author") long author);
//SELECT * FROM myblogc3.post as p WHERE( (SELECT  p.content REGEXP binary  'Lorem') or (SELECT  p.title REGEXP binary 'Lorem' ) );
@Query(
		value="SELECT "
		+ "p.id"
		+ " FROM Post as p "
		+ "WHERE ( REGEXP_LIKE(p.content,:key )"
		+ "or REGEXP_LIKE(p.title, :key )) AND p.is_visible = true"
		,nativeQuery=true
		)
List<Long> getNoPostCaseSensitiveAndExacSensitive(@Param("key")String keyword);
@Query(
		value="SELECT "
		+ "p.id"
		+ " FROM Post as p "
		+ "WHERE ( REGEXP_LIKE(p.content,binary :key )"
		+ "or REGEXP_LIKE(p.title,binary :key )) AND p.is_visible = true"
		,nativeQuery=true
		)
List<Long> getPostCaseSensitiveAndExacSensitive(@Param("key")String keyword);


@Query("SELECT new it.course.myblogc3.payload.response.PostResponseForSearch("
		+ "p.id,"
		+ "p.title,"
		+ "p.content,"
		+ "p.author.username,"
		+ "p.updateAt) "
		+ "FROM Post p "
		+ "WHERE p.id IN :ids"
		+ "")
List<PostResponseForSearch> getPostByIdAnVisbleIsTrue(@Param("ids") List<Long> ids);

//SELECT * FROM myblogc3.post as p WHERE( (SELECT  p.content REGEXP  'LoRem') or (SELECT  p.title REGEXP  'LoRem' ) );
@Query(value="SELECT p.* FROM post p "
		+ " WHERE p.is_visible=true "
		+ " AND   REGEXP_LIKE(p.title, :wordToFind ) OR REGEXP_LIKE(p.content,:wordToFind )",nativeQuery = true)
List<Post> getPostsVisibleBySearchCaseSensitiveFalse(String wordToFind);


@Query(value="SELECT p.* FROM post p "
		+ " WHERE p.is_visible=true "
		+ " AND   REGEXP_LIKE(p.title, BINARY :wordToFind ) OR REGEXP_LIKE(p.content, BINARY :wordToFind )",nativeQuery = true)
List<Post> getPostsVisibleBySearchCaseSensitiveTrue(String wordToFind);

@Query(value="SELECT p.author.username "
		+ "FROM Post p "
		+ "WHERE p.id = :postId ")
String getAuthorUsernameByPostId(long postId);

@Query("SELECT new it.course.myblogc3.payload.response.PostResponseForSearch("
		+ "p.id,"
		+ "p.title,"
		+ "p.content,"
		+ "p.author.username,"
		+ "p.updateAt) "
		+ "FROM Post p "
		+ "WHERE p.visible = true"
		+ "")
List<PostResponseForSearch> getPostsForSearch();

@Query(value ="SELECT p "
		+ "FROM Post p "
		+ " JOIN FETCH p.tags pt    "
		+ " JOIN FETCH p.author u  "
		+ "	"
		+ "WHERE p.visible = true and pt.tagName IN (:tagsName)	and pt.enable=1"
		+ "GROUP BY p.id "
		+ "ORDER BY p.updateAt  "
		+ "")
	List<Post>  getPostsVisibleByTag(@Param("tagsName") Set<String> tagsName);

@Query(value="SELECT p1 FROM Post p1 "
		+ "JOIN FETCH p1.tags "
		+ "JOIN FETCH p1.author u "
		+ "WHERE p1 IN (SELECT p from Post p "
		+ "JOIN p.tags pt "
		+ "JOIN User u on u.id= :userId "
		+ "JOIN u.preferredTags upt ON upt.tagName=pt.tagName "
		+ "WHERE p.visible = true "
		+ "AND pt.enable=true)"
		)
Set<Post> getPostsVisibleByPreferredTags(@Param("userId") long userId);


}
