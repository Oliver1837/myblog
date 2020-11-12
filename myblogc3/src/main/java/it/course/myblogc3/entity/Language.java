package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="LANGUAGE")
@Data @AllArgsConstructor @NoArgsConstructor 
@EqualsAndHashCode(callSuper = false)
//notazioni lombok genera  i getter/setter,costruttore con e senza argomenti  senza vederli
public class Language {
	//chiave primaria naturale
	@Id
	@Column(name="LANG_CODE",columnDefinition="VARCHAR(2)")
	private String langCode;//Chiave primaria Naturale
	
	@Column(name="LANG_DESC",nullable=true,unique=true,columnDefinition="VARCHAR(15)")
	private String langDesc;// Descrizione
	
	@Column(name="IS_VISIBLE")
	private Boolean visible=true;

	
	//costruttore
	public Language(String langCode, String langDesc) {
		super();
		this.langCode = langCode;
		this.langDesc = langDesc;
	}
	
	
	
}
