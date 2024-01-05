package edu.uclm.esi.backJuegos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.backJuegos.model.Word;

@Repository
public interface WordRepository extends JpaRepository <Word, String>{

	@Query(value = "SELECT w FROM Word w WHERE w.length > :length")
	public List<Word> wordsByLength(@Param("length") Integer length);
	
}
