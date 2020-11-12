package it.course.myblogc3.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TOKEN")
@Data @AllArgsConstructor @NoArgsConstructor
public class Token {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	
	@Column(columnDefinition="TEXT",unique = true ,nullable =false)
	private String token;

	@Column(nullable=false)
	private Date expiryDate;
	
	
	public Token(String token,Date expiryDate) {
		super();
		this.token = token;
		this.expiryDate=expiryDate;
	}
	
	
	
	
}
