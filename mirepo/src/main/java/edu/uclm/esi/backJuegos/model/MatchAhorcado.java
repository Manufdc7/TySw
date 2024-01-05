package edu.uclm.esi.backJuegos.model;

public class MatchAhorcado extends Match {
	
	private String word;

	public MatchAhorcado() {
		super();
	}

	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public boolean isReady() {
		return this.getPlayers().size()==2;
	}

}
