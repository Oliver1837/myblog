package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity// definizione entità
@Table(name="AUTHORITIES")//definizione tabella con nome
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Authority {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME", length=15, nullable=false)
	@Enumerated(EnumType.STRING)
	private AuthorityName name;
	
}
