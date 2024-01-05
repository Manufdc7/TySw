package edu.uclm.esi.backJuegos.model;

import java.util.Base64;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = {
	@Index(unique = true, columnList = "email"),
	@Index(unique = true, columnList = "name"),
})
public class User {
	@Id
	private String id;
	@NotNull 
	private String email;
	@NotNull
	private String name;
	@NotNull
	private String pwd;
	private Long confirmationDate;
	
	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String userName) {
		this.name = userName;
	}

	@JsonIgnore
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd =  org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}
	
	public Long getConfirmationDate() {
		return confirmationDate;
	}
	
	public void setConfirmationDate(Long confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

}
