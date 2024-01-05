package edu.uclm.esi.backJuegos.webSockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.backJuegos.constants.ConstantKeys;
import edu.uclm.esi.backJuegos.http.Manager;
import edu.uclm.esi.backJuegos.model.Match;
import edu.uclm.esi.backJuegos.model.MatchAhorcado;
import edu.uclm.esi.backJuegos.model.Result;
import edu.uclm.esi.backJuegos.model.User;
import edu.uclm.esi.backJuegos.sessions.HWSession;
import edu.uclm.esi.backJuegos.utils.Utils;

@Component
public class WSGames extends TextWebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String query = session.getUri().getQuery();
		String httpSessionId = query.substring("httpSessionId=".length());
		Manager.get().setWebsocketSession(httpSessionId, session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		JSONObject jso = new JSONObject(message.getPayload());
		String type = jso.getString(ConstantKeys.TYPE);

		if (type.equals(ConstantKeys.PLAYER_READY)) {
			String matchId = jso.getString(ConstantKeys.MATCH_ID);
			Match match = Manager.get().getMatch(matchId);

			if (match != null) {
				synchronized (match) {
					if (!match.isStarted()) {
						match.setStarted(true);
						if (match instanceof MatchAhorcado) {
							String word = ((MatchAhorcado) match).getWord();
							match.notifyStartAhorcado(word);
						} else {
							match.notifyStart();
						}
					}
				}
			} else {
				notifyYourTurn(session);
			}
		} else if (type.equals(ConstantKeys.MOVE_4ENRAYA)) {

			Match match = Manager.get().getMatch(jso.getString(ConstantKeys.MATCH_ID));
			String userId = jso.getString(ConstantKeys.USER_ID);
			ObjectMapper objectMapper = new ObjectMapper();
			String status = ConstantKeys.STATUS_CONTINUE;
			String currentPlayer = jso.getString(ConstantKeys.CURRENT_PLAYER);
			String boardStr = jso.getString(ConstantKeys.BOARD);
			String[][] board = objectMapper.readValue(boardStr, String[][].class);

			if (Utils.isTie(board)) {
				status = ConstantKeys.STATUS_TIE;
				match.notifyEnd(status, boardStr, userId);
				Manager.get().removeMatch(match.getId());

				for (User user : match.getPlayers()) {
					Result res = Manager.get().getResultsRepository().findByUserAndType(user.getId(),
							ConstantKeys.CUATROENRAYA);
					if (res != null) {
						res.setEmpates(res.getEmpates() + 1);
						Manager.get().getResultsRepository().save(res);
					} else {
						Result newRes = new Result();
						newRes.setDerrotas(0);
						newRes.setType(ConstantKeys.CUATROENRAYA);
						newRes.setVictorias(0);
						newRes.setEmpates(1);
						newRes.setUser(user.getId());
						Manager.get().getResultsRepository().save(newRes);
					}
				}
			}

			if (Utils.isWinner(currentPlayer, board)) {
				status = ConstantKeys.STATUS_WIN;
				match.notifyEnd(status, boardStr, userId);
				Manager.get().removeMatch(match.getId());

				for (User user : match.getPlayers()) {
					Result res = Manager.get().getResultsRepository().findByUserAndType(user.getId(),
							ConstantKeys.CUATROENRAYA);
					if (res != null) {
						if (userId.equals(user.getId())) {
							res.setVictorias(res.getVictorias() + 1);
						} else {
							res.setDerrotas(res.getDerrotas() + 1);
						}
						Manager.get().getResultsRepository().save(res);
					} else {
						Result newRes = new Result();
						newRes.setType(ConstantKeys.CUATROENRAYA);
						newRes.setEmpates(0);
						newRes.setUser(user.getId());
						if (userId.equals(user.getId())) {
							newRes.setDerrotas(0);
							newRes.setVictorias(1);
						} else {
							newRes.setDerrotas(1);
							newRes.setVictorias(0);
						}

						Manager.get().getResultsRepository().save(newRes);
					}
				}
			}

			if (status == ConstantKeys.STATUS_CONTINUE) {
				HWSession hwSession = null;
				for (User user : match.getPlayers()) {
					if (!userId.equals(user.getId())) {
						hwSession = Manager.get().getSessionByUserId(user.getId());
						WebSocketSession wsSession = hwSession.getWebsocketSession();
						JSONObject jsoContinue = new JSONObject().put(ConstantKeys.TYPE, status).put(ConstantKeys.BOARD,
								boardStr);
						TextMessage continueMessage = new TextMessage(jsoContinue.toString());
						try {
							wsSession.sendMessage(continueMessage);
						} catch (IOException e) {
							System.err.println("In notify: " + session.getId() + "-> " + e.getMessage());
						}
					}
				}

			}

		} else if (type.equals(ConstantKeys.MOVE_AHORCADO)) {

			List<String> list = new ArrayList<>();
			Match match = Manager.get().getMatch(jso.getString(ConstantKeys.MATCH_ID));
			String userId = jso.getString(ConstantKeys.USER_ID);
			String letter = jso.getString(ConstantKeys.LETTER);

			list.add(letter);
			if (match instanceof MatchAhorcado) {
				list.add(((MatchAhorcado) match).getWord());
			}

			List<Boolean> result = Utils.guessLetter(list);
			ObjectMapper objectMapper = new ObjectMapper();
			String resultStr = objectMapper.writeValueAsString(result);
			match.notifyMoveAhorcado(ConstantKeys.STATUS_CONTINUE, resultStr, letter, userId);

		} else if (type.equals(ConstantKeys.RESOLVE_AHORCADO)) {

			List<String> list = new ArrayList<>();
			Match match = Manager.get().getMatch(jso.getString(ConstantKeys.MATCH_ID));
			String userId = jso.getString(ConstantKeys.USER_ID);
			String word = jso.getString(ConstantKeys.WORD);

			list.add(word);
			if (match instanceof MatchAhorcado) {
				list.add(((MatchAhorcado) match).getWord());
			}

			Boolean result = Utils.guessWord(list);
			match.notifyMoveAhorcado(ConstantKeys.STATUS_WIN, result.toString(), word, userId);
			Manager.get().removeMatch(match.getId());

			for (User user : match.getPlayers()) {
				Result res = Manager.get().getResultsRepository().findByUserAndType(user.getId(),
						ConstantKeys.AHORCADO);
				if (res != null) {
					if (result) {
						res.setVictorias(res.getVictorias() + 1);
					} else {
						res.setDerrotas(res.getDerrotas() + 1);
					}
					Manager.get().getResultsRepository().save(res);
				} else {
					Result newRes = new Result();
					newRes.setType(ConstantKeys.AHORCADO);
					newRes.setEmpates(0);
					newRes.setUser(user.getId());
					if (result) {
						newRes.setDerrotas(0);
						newRes.setVictorias(1);
					} else {
						newRes.setDerrotas(1);
						newRes.setVictorias(0);
					}
					Manager.get().getResultsRepository().save(newRes);
				}
			}

		} else if (type.equals(ConstantKeys.END_AHORCADO)) {

			Match match = Manager.get().getMatch(jso.getString(ConstantKeys.MATCH_ID));
			Boolean result = jso.getBoolean(ConstantKeys.RESULT);
			Manager.get().removeMatch(match.getId());

			for (User user : match.getPlayers()) {
				Result res = Manager.get().getResultsRepository().findByUserAndType(user.getId(),
						ConstantKeys.AHORCADO);
				if (res != null) {
					if (result) {
						res.setVictorias(res.getVictorias() + 1);
					} else {
						res.setDerrotas(res.getDerrotas() + 1);
					}
					Manager.get().getResultsRepository().save(res);
				} else {
					Result newRes = new Result();
					newRes.setType(ConstantKeys.AHORCADO);
					newRes.setEmpates(0);
					newRes.setUser(user.getId());
					if (result) {
						newRes.setDerrotas(0);
						newRes.setVictorias(1);
					} else {
						newRes.setDerrotas(1);
						newRes.setVictorias(0);
					}
					Manager.get().getResultsRepository().save(newRes);
				}
			}

		} else if (type.equals(ConstantKeys.CHAT)) {

			Match match = Manager.get().getMatch(jso.getString(ConstantKeys.MATCH_ID));
			String userId = jso.getString(ConstantKeys.USER_ID);
			String chat = jso.getString(ConstantKeys.MESSAGE);
			String userName = jso.getString(ConstantKeys.USER_NAME);

			HWSession hwSession = null;
			for (User user : match.getPlayers()) {
				if (!userId.equals(user.getId())) {
					hwSession = Manager.get().getSessionByUserId(user.getId());
					WebSocketSession wsSession = hwSession.getWebsocketSession();
					JSONObject jsoChat = new JSONObject().put(ConstantKeys.TYPE, ConstantKeys.CHAT)
							.put(ConstantKeys.MESSAGE, chat).put(ConstantKeys.USER_NAME, userName);
					TextMessage continueMessage = new TextMessage(jsoChat.toString());
					try {
						wsSession.sendMessage(continueMessage);
					} catch (IOException e) {
						System.err.println("In notify: " + session.getId() + "-> " + e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		HWSession hwSession = Manager.get().getSessionByWebSocketId(session.getId());
		String userId = hwSession.getUserId();
		Match match = Manager.get().getMatchByUserId(userId);
		String gameType;
		if (match != null) {
			if (match instanceof MatchAhorcado) {
				gameType = ConstantKeys.AHORCADO;
			} else {
				gameType = ConstantKeys.CUATROENRAYA;
			}
			for (User user : match.getPlayers()) {
				Result res = Manager.get().getResultsRepository().findByUserAndType(user.getId(), gameType);
				if (!userId.equals(user.getId())) {
					HWSession hwSession2player = Manager.get().getSessionByUserId(user.getId());
					WebSocketSession wsSession = hwSession2player.getWebsocketSession();
					JSONObject jsoLeft = new JSONObject().put(ConstantKeys.TYPE, ConstantKeys.LEFT);
					TextMessage leftMessage = new TextMessage(jsoLeft.toString());

					if (res != null) {
						if (gameType.equals(ConstantKeys.AHORCADO)) {
							res.setDerrotas(res.getDerrotas() + 1);
						} else {
							res.setVictorias(res.getVictorias() + 1);
						}
						Manager.get().getResultsRepository().save(res);
					} else {
						Result newRes = new Result();
						newRes.setType(gameType);
						newRes.setEmpates(0);
						newRes.setUser(user.getId());
						if (gameType.equals(ConstantKeys.AHORCADO)) {
							newRes.setDerrotas(1);
							newRes.setVictorias(0);
						} else {
							newRes.setDerrotas(0);
							newRes.setVictorias(1);
						}
						Manager.get().getResultsRepository().save(newRes);
					}

					try {
						wsSession.sendMessage(leftMessage);
					} catch (IOException e) {
						System.err.println("In notify: " + session.getId() + "-> " + e.getMessage());
					}
				} else {

					if (res != null) {
						res.setDerrotas(res.getDerrotas() + 1);
						Manager.get().getResultsRepository().save(res);
					} else {
						Result newRes = new Result();
						newRes.setType(gameType);
						newRes.setEmpates(0);
						newRes.setUser(user.getId());
						newRes.setDerrotas(1);
						newRes.setVictorias(0);
						Manager.get().getResultsRepository().save(newRes);
					}
				}
			}
			Manager.get().removeMatch(match.getId());
		}
		hwSession.setWebsocketSession(null);
	}

	public void notifyYourTurn(WebSocketSession session) {
		this.notifyOnePlayer(session, ConstantKeys.YOUR_TURN);
	}

	private void notifyOnePlayer(WebSocketSession session, String type, Object... keyValue) {
		JSONObject jso = new JSONObject().put(ConstantKeys.TYPE, type);
		for (int i = 0; i < keyValue.length; i++) {
			jso.put(keyValue[i].toString(), keyValue[i + 1]);
			i++;
		}
		TextMessage message = new TextMessage(jso.toString());
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			System.err.println("In notify: " + session.getId() + "-> " + e.getMessage());
		}
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

}
