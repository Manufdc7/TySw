package edu.uclm.esi.backJuegos.http;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.backJuegos.constants.ConstantKeys;
import edu.uclm.esi.backJuegos.model.Match;
import edu.uclm.esi.backJuegos.model.Word;
import edu.uclm.esi.backJuegos.services.CuatroEnRayaService;
import edu.uclm.esi.backJuegos.services.WaitingRoomService;
import edu.uclm.esi.backJuegos.services.WordService;
import edu.uclm.esi.backJuegos.webdto.CheckStatusDto;
import edu.uclm.esi.backJuegos.webdto.ResultDto;

@RestController
@RequestMapping("games")
@CrossOrigin(origins = "*")
public class GamesController extends CookiesController {

	@Autowired
	private WordService wordService;

	@Autowired
	private CuatroEnRayaService cuatroEnRayaService;
	
	@Autowired
	private WaitingRoomService waitingRoomService;

	@PostMapping("/ahorcado/getWord")
	public ResponseEntity<Word> getWord(@RequestBody Integer length) {

		Word word = wordService.getRandomWord(length);
		return ResponseEntity.ok(word);
	}

	@PostMapping("/ahorcado/guessLetter")
	public ResponseEntity<List<Boolean>> guessLetter(@RequestBody List<String> list) {

		List<Boolean> result = wordService.guessLetter(list);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/ahorcado/guessWord")
	public ResponseEntity<Boolean> guessWord(@RequestBody List<String> list) {

		Boolean result = wordService.guessWord(list);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/cuatroEnRaya/checkStatus")
	public ResponseEntity<ResultDto> checkCuatroEnRayaStatus(@RequestBody CheckStatusDto checkStatusDto) throws JsonMappingException, JsonProcessingException {

		ResultDto result = new ResultDto();
		ObjectMapper objectMapper = new ObjectMapper();
		String status = ConstantKeys.STATUS_CONTINUE;
		String [][] board = objectMapper.readValue(checkStatusDto.getBoard(), String[][].class);

		if (cuatroEnRayaService.isTie(board)) {
			status = ConstantKeys.STATUS_TIE;
		}  
		
		if (cuatroEnRayaService.isWinner(checkStatusDto.getCurrentPlayer(), board)) {
			status = ConstantKeys.STATUS_WIN;
		}
		
		result.setResult("OK");
		result.setMessage(status);

		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/playMatch")
	public ResponseEntity<Match> playMatch(HttpSession session, @RequestBody Map<String, Object> model) {
		
		JSONObject jso = new JSONObject(model);
		String userId = jso.getString("userId");
		String gameName = jso.getString("gameName");
		HttpSession existingSession = Manager.get().findHttpSession(userId);
		
		if(existingSession != null) {
			Match match = this.waitingRoomService.playMatch(userId, gameName);
			return ResponseEntity.ok(match);
		}else {
			return ResponseEntity.ok(null);
		}
	}
	
	@PostMapping("/removeMatch")
	public void removeMatch(HttpSession session, @RequestBody Map<String, Object> model) {
		JSONObject jso = new JSONObject(model);
		String matchId = jso.getString("matchId");
		String gameName = jso.getString("gameName");
		this.waitingRoomService.removeMatch(matchId, gameName);
	}

}
