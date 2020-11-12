package it.course.myblogc3.repository;

import org.springframework.stereotype.Repository;
import it.course.myblogc3.entity.Language;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LanguageRepository extends JpaRepository<Language,String>{

	
	boolean existsByLangCodeOrLangDesc(String langCode,String langDesc);
	
	
	  @Query("UPDATE Language	l	" +
	  "	SET	" + 
	  " l.langDesc = :x" +
	  "	WHERE l.langCode = :langCode") 
	  
	  int updateLangDesc(@Param("x")String langDesc,@Param("langCode")String langCode);
	 
	 Optional<Language> findByLangCodeAndVisibleTrue(String langCode);
}
