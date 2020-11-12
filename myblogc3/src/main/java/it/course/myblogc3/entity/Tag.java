package it.course.myblogc3.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TAG")
@Data @AllArgsConstructor @NoArgsConstructor

public class Tag {

	@Id
	@Column(name="TAG_NAME", length=15)
	private String tagName;
	
	@Column(name="IS_ENABLE",	nullable = false)//is visible
	private Boolean enable =true;

	
	
	
	
	
	
}
