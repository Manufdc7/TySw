package edu.uclm.esi.backJuegos.webdto;

import org.json.JSONObject;

public class CheckStatusDto {

	private String board;
	private String currentPlayer;

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
}
