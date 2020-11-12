package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Authority;
import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);	
	
	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Boolean existsByUsernameOrEmail(String username, String email);
	
	List<User> findAllByEnabledTrue();
	
	//List<User> findByEmailOrUsernameNotIn();
	
	Optional<User> findByIdAndEnabledTrue(Long id);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByUsernameAndEnabledTrue(String username);
    
    Boolean existsByUsername(String username);
    
	Boolean existsByEmail(String email);
	
	
	
	Optional<User> findByIdentifierCode(String identifierCode);
	
	Optional<User> findByIdentifierCodeAndEmail(String identifierCode,String email);
	
	Optional<User> findByRegistrationConfirmCodeAndEmail(String registrationConfirmCode,String email);
    
@Query(value=" Select new User( "
		+ "u.id"	
		+ ")"
		+ "FROM User u "
		+ "LEFT JOIN u.authorities a"
		+ " WHERE a.id = :id"
		+ "")
List<User> findByRole(@Param("id")long  idEditor);

	@Query(value=" SELECT CASE WHEN EXISTS ("
			+ "    SELECT * "
			+ "    FROM User as u"
			+ "    WHERE u.id =:id and (u.banned_util>=CURRENT_TIMESTAMP or u.banned_util IS NOT NULL)"
			+ ")"
			+ "THEN 0 "
			+ "ELSE 1  END",nativeQuery = true)
	int isAlreadyBan(@Param("id") Long id);

	
	@Query("Select u.preferredTags"
			+ "	FROM User u"
			+ "	WHERE u.id=:id"
			
			)
	Set<Tag> getUserTags(@Param("id")Long idUser);
}
