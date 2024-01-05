package edu.uclm.esi.backJuegos.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.backJuegos.dao.WordRepository;
import edu.uclm.esi.backJuegos.model.Word;

@Service
public class WordService {

	@Autowired
	WordRepository wordRepository;

	public Word getRandomWord(Integer length) {

		List<Word> wordsLen = wordRepository.wordsByLength(length);
		int pos = new SecureRandom().nextInt(wordsLen.size());
		Word word = wordsLen.get(pos);
		return word;

	}

	public List<Boolean> guessLetter(List<String> list) {
		
	    List<Boolean> result = new ArrayList<>();
	    
	    if (list.size() != 2) {
	        return result; 
	    }

	    String letra = list.get(0);
	    String palabra = list.get(1);

	    for (int i = 0; i < palabra.length(); i++) {
	        if (palabra.substring(i, i + 1).equals(letra)) {
	            result.add(true);
	        } else {
	            result.add(false);
	        }
	    }

	    return result;
	}
	
	public Boolean guessWord(List<String> word) {
		
		boolean result = false;
		
		if(word.get(0).equals(word.get(1))) {
			result = true;
		}
		
		return result;
	}

}
