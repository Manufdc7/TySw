package edu.uclm.esi.backJuegos.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.backJuegos.constants.ConstantKeys;
import edu.uclm.esi.backJuegos.http.Manager;
import edu.uclm.esi.backJuegos.sessions.HWSession;

public abstract class Match {

	protected String id;
	protected User winner;
	protected List<User> players;
	protected boolean started;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
	}

	public static Match newMatch(String gameName, User user) {

		Match match = null;
		String className = "Match" + gameName;

		try {
			Class<Match> clazz = (Class<Match>) Class.forName("edu.uclm.esi.backJuegos.model." + className);
			match = clazz.newInstance();
			match.players.add(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return match;
	}

	public void notifyStart() {
		this.notify(ConstantKeys.MATCH_STARTED);
	}

	public void notifyStartAhorcado(String word) {
		this.notify(ConstantKeys.MATCH_STARTED, ConstantKeys.WORD, word);
	}

	public void notifyMoveAhorcado(String status, String result, String letter, String userId) {
		this.notify(status, ConstantKeys.BOARD, result, ConstantKeys.LETTER, letter,
				ConstantKeys.USER_ID, userId);
	}

	public void notifyEnd(String status, String board, String userId) {
		this.notify(status, ConstantKeys.BOARD, board, ConstantKeys.USER_ID, userId);
	}

	public void move(JSONObject jsoMovement, String userId) {
		if (this.winner == null)
			this.notifyUpdate(userId);
		else
			this.notify(ConstantKeys.MATCH_FINISHED, "winner", this.winner.getName());
	}

	private void notifyUpdate(String userId) {
		this.notify("MATCH UPDATE", "userId", userId);
	}

	private void notify(String type, Object... keyValue) {
		JSONObject jso = new JSONObject().put(ConstantKeys.TYPE, type).put(ConstantKeys.MATCH_ID, this.id);
		for (int i = 0; i < keyValue.length; i++) {
			jso.put(keyValue[i].toString(), keyValue[i + 1]);
			i++;
		}
		for (User player : players) {
			HWSession hwSession = Manager.get().getSessionByUserId(player.getId());
			WebSocketSession wsSession = hwSession.getWebsocketSession();
			synchronized (wsSession) {
				TextMessage message = new TextMessage(jso.toString());
				try {
					wsSession.sendMessage(message);
				} catch (IOException e) {
					System.err.println("In notify: " + wsSession.getId() + "-> " + e.getMessage());
				}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public abstract boolean isReady();

}
