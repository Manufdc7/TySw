package edu.uclm.esi.backJuegos.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Result {
	
	@Id
	private String id;
	private String user;
	private String type;
	private Integer victorias;
	private Integer derrotas;
	private Integer empates;
	
	
	public Result() {
		this.id = UUID.randomUUID().toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getVictorias() {
		return victorias;
	}
	public void setVictorias(Integer victorias) {
		this.victorias = victorias;
	}
	public Integer getDerrotas() {
		return derrotas;
	}
	public void setDerrotas(Integer derrotas) {
		this.derrotas = derrotas;
	}
	public Integer getEmpates() {
		return empates;
	}
	public void setEmpates(Integer empates) {
		this.empates = empates;
	}

}
