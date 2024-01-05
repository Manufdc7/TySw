package edu.uclm.esi.backJuegos.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.backJuegos.model.Result;

public interface ResultRepository extends JpaRepository<Result, String> {

	public Result findByUserAndType(String userId, String type);
	
	public Result findByVictorias(Integer victorias);
	
}
