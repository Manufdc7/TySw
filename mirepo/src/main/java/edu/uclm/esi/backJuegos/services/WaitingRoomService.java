package edu.uclm.esi.backJuegos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.backJuegos.dao.UserRepository;
import edu.uclm.esi.backJuegos.http.Manager;
import edu.uclm.esi.backJuegos.model.Match;
import edu.uclm.esi.backJuegos.model.MatchAhorcado;
import edu.uclm.esi.backJuegos.model.User;
import edu.uclm.esi.backJuegos.model.Word;

@Service
public class WaitingRoomService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WordService wordService;
	
	private ConcurrentHashMap<String, List<Match>> pendingMatches;

	public WaitingRoomService() {
		this.pendingMatches = new ConcurrentHashMap<>();
	}

	public Match playMatch(String userId, String gameName) {
		
		Match match = null;
		User user = this.userRepository.findById(userId).get();
		List<Match> matches = this.pendingMatches.get(gameName);
		
		if (matches == null || matches.isEmpty()) {
			matches = new ArrayList<>();
			match = Match.newMatch(gameName, user);
			matches.add(match);
			this.pendingMatches.put(gameName, matches);
		} else {
			match = matches.get(0);
			List<User> players = match.getPlayers();
			players.add(user);
			match.setPlayers(players);
			if(match.isReady()) {
				matches.remove(0);
				if (match instanceof MatchAhorcado) {			
					Random random = new Random();
			        int randomNumber = random.nextInt((15 - 8) + 1) + 8;
			        Integer randomInteger = Integer.valueOf(randomNumber);	                
					Word word = wordService.getRandomWord(randomInteger);
	                ((MatchAhorcado) match).setWord(word.getWord());
	            }
				Manager.get().add(match);
			}
		}
		
		return match;
	}
	
	public boolean removeMatch(String matchId, String gameName) {
		
		Match match = null;
		List<Match> matches = this.pendingMatches.get(gameName);
        if (matches != null) {          
            for (Match m : matches) {
                if (matchId.equals(m.getId())) {
                    match = m; 
                }
            }
            if(match != null) {
            	matches.remove(match);
            }
        }
        return true;
	}

}
