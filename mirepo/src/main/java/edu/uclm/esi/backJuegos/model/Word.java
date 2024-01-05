package edu.uclm.esi.backJuegos.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Word {

	@Id
	private Integer id;
	private String word;
	private Integer length;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
}
