package edu.uclm.esi.backJuegos.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.backJuegos.dao.ResultRepository;
import edu.uclm.esi.backJuegos.dao.TokenRepository;
import edu.uclm.esi.backJuegos.dao.UserRepository;
import edu.uclm.esi.backJuegos.model.Match;
import edu.uclm.esi.backJuegos.model.User;
import edu.uclm.esi.backJuegos.sessions.HWSession;

@Component
public class Manager {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokensRepository;
	@Autowired
	private ResultRepository resultRepository;

	private ConcurrentHashMap<String, User> users;
	private JSONObject configuration;
	private Map<String, HttpSession> httpSessions;
	private ConcurrentHashMap<String, HWSession> sessionsByUserId;
	private ConcurrentHashMap<String, HWSession> sessionsByHttpId;
	private ConcurrentHashMap<String, HWSession> sessionsByWsId;
	private ConcurrentHashMap<String, Match> matches;

	private Manager() {
		this.users = new ConcurrentHashMap<>();
		this.httpSessions = new ConcurrentHashMap<>();
		try {
			loadParameters();
		} catch (Exception e) {
			System.err.println("Error al leer el fichero parametros.txt: " + e.getMessage());
			System.exit(-1);
		}
		this.sessionsByUserId = new ConcurrentHashMap<>();
		this.sessionsByHttpId = new ConcurrentHashMap<>();
		this.sessionsByWsId = new ConcurrentHashMap<>();
		this.matches = new ConcurrentHashMap<>();
	}

	private static class ManagerHolder {
		static Manager singleton = new Manager();
	}

	@Bean
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public JSONObject getConfiguration() {
		return configuration;
	}

	private void loadParameters() throws IOException {
		this.configuration = read("./parametros.txt");
	}

	private JSONObject read(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			String s = new String(b);
			return new JSONObject(s);
		}
	}

	public String readTextFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		}
	}

	public void addSessionByUserId(String userId, HttpSession httpSession) {
		HWSession hwSession = new HWSession(httpSession);
		this.sessionsByUserId.put(userId, hwSession);
		this.sessionsByHttpId.put(httpSession.getId(), hwSession);
	}

	public void setWebsocketSession(String httpSessionId, WebSocketSession websocketSession) {
		HWSession hwSession = this.sessionsByHttpId.get(httpSessionId);
		hwSession.setWebsocketSession(websocketSession);
		this.sessionsByWsId.put(websocketSession.getId(), hwSession);
	}

	public HWSession getSessionByUserId(String userId) {
		return this.sessionsByUserId.get(userId);
	}

	public HWSession removeSessionByUserId(String userId) {
		HWSession hwSession = this.sessionsByUserId.remove(userId);
		this.sessionsByHttpId.remove(hwSession.getHttpSession().getId());
		if (hwSession.getWebsocketSession() != null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}

	public HWSession getSessionByHttpId(String httpId) {
		return this.sessionsByHttpId.get(httpId);
	}

	public HWSession getSessionByWebSocketId(String wsId) {
		return this.sessionsByWsId.get(wsId);
	}

	public HWSession removeSessionByHttpId(String httpId) {
		HWSession hwSession = this.sessionsByHttpId.remove(httpId);
		this.sessionsByUserId.remove(hwSession.getUserId());
		if (hwSession.getWebsocketSession() != null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}

	public void invalidate(HWSession existingSession) {
		existingSession.getHttpSession().invalidate();
		try {
			existingSession.getWebsocketSession().close();
		} catch (IOException e) {
		}
		this.removeSessionByHttpId(existingSession.getHttpSession().getId());
	}

	public void add(User user) {
		this.users.put(user.getName(), user);
	}

	public void add(Match match) {
		this.matches.put(match.getId(), match);
	}

	public Match getMatch(String matchId) {
		return this.matches.get(matchId);
	}

	public Match removeMatch(String matchId) {
		return this.matches.remove(matchId);
	}

	public User findUser(String userName) {
		return this.users.get(userName);
	}

	public User removeUser(String userName) {
		return this.users.remove(userName);
	}

	public UserRepository getUsersRepository() {
		return this.userRepository;
	}
	
	public ResultRepository getResultsRepository() {
		return this.resultRepository;
	}

	public TokenRepository getTokensRepository() {
		return tokensRepository;
	}

	public HttpSession findHttpSession(String userId) {
		return this.httpSessions.get(userId);
	}

	public void addHttpSession(String userId, HttpSession session) {
		this.httpSessions.put(userId, session);
	}
	
	public void removeHttpSession(String userId) {
		this.httpSessions.remove(userId);
	}
	
	public Match getMatchByUserId(String userId) {
	    for (Match match : this.matches.values()) {
	        for (User player : match.getPlayers()) {
	            if (player.getId().equals(userId)) {
	                return match;
	            }
	        }
	    }
	    return null; 
	}

}
