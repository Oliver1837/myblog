package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.payload.response.PostDetailTagResponse;

@Repository
public interface TagRepository extends JpaRepository<Tag,String>{
		
	@Query(value="SELECT p.tags FROM Post p WHERE p.id = :x"
			+ ""
			+ ""
			+ "")
	Set<Tag> getTagByIdPost(@Param("x") long id);
	/*
	 * @Query(value="SELECT t.tagName FROM Tag t  WHERE t.enable=true " + "" + "" +
	 * "") String findByTagNameInAndEnableTrue();
	 */
	
	Set<Tag> findByTagNameInAndEnableTrue(Set<String> tagName);
	
	Set<Tag> findByEnableTrue();
	
	Optional<Tag> findByTagNameAndEnableTrue(String tagName);
	@Query(value="SELECT pt.tag_id  "
			+ "FROM (Post p left JOIN post_TAGS  pt on p.id = pt.post_id ) left join tag t on pt.tag_id = t.tag_name WHERE p.id = :x AND t.is_enable = 1"
			+ ""
			+ ""
			+ "",nativeQuery=true)
	String[] getTagStringByIdPost(@Param("x") Long id);
}
