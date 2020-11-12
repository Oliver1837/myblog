package it.course.myblogc3.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="COMMENT")
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	/*
	 * @Column(name="CREATED_AT" , updatable=false , insertable=false ,
	 * columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	 */
	@Column(name="CREATED_AT"
			, updatable=false 
			, insertable=false
			, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@Column(name="COMMENT" , nullable= false, length=200)
	private String comment;
	
	@Column(name="IS_VISIBLE" , nullable=false)
	private Boolean visible = true;
	
	  @ManyToOne (fetch = FetchType.LAZY)
	  @JoinColumn(name="COMMENT_AUTHOR" , nullable=false)
	private User commentAuthor;

	@ManyToOne(fetch=FetchType.LAZY)
	private Post post;
	

	
	
	
	 public Comment(String comment,Post post,User user) { 
		 super(); 
		 this.comment =comment; 
		 this.post = post; //this.depth=depth; 
		 this.commentAuthor = user;
	}
	

	public Comment(String comment, Boolean visible, User commentAuthor, Post post) {
		super();
		this.comment = comment;
		this.visible = visible;
		this.commentAuthor = commentAuthor;
		this.post = post;
	}


	


	
	
}
