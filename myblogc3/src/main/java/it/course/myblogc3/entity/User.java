package it.course.myblogc3.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

import it.course.myblogc3.entity.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="User")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User extends DateAudit{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NaturalId(mutable=true)
	@Column(name="EMAIL", nullable=false, length=120)
	private String email;
	
	@Column(name="USERNAME", nullable=false, unique=true , length=12)
	private String username;
	
	@Column(name="PASSWORD", nullable=false, unique=true, length=100)
	private String password;
	
	@Column(name="IS_ENABLE", columnDefinition="TINYINT(1)")
	private Boolean enabled =false;

	
	//@Column(name="INDENTIFIER_CODE", nullable=false, unique=true, length=100)
	private String identifierCode;//riferimento se un utente deve cambiare password
	
	private String registrationConfirmCode;
	
	private LocalDateTime bannedUtil;

	@Column(nullable = false, columnDefinition="INT(6)")
	private int credits = 0;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_AUTHORITIES",
		joinColumns =  { @JoinColumn(name="USER_ID",referencedColumnName = "ID") },
		inverseJoinColumns = {@JoinColumn (name="AUTHORITY_ID" , referencedColumnName = "ID")}
		)
	private Set<Authority> authorities;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="USER_PREFERRED_TAGS",
		joinColumns =  { @JoinColumn(name="USER_ID",referencedColumnName = "ID") },
		inverseJoinColumns = {@JoinColumn (name="TAG_ID" , referencedColumnName = "TAG_NAME")}
		)
	private Set<Tag> preferredTags;
	
	 
	
	public User(String email, String username, String password) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}


	public User(Long id) {
		super();
		this.id = id;
	}




	
	
}
