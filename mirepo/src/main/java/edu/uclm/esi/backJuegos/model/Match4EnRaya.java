package edu.uclm.esi.backJuegos.model;

public class Match4EnRaya extends Match {

	public Match4EnRaya() {
		super();
	}

	@Override
	public boolean isReady() {
		return this.getPlayers().size()==2;
	}

}
