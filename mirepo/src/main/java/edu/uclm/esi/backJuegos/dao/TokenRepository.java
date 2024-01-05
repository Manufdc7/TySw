package edu.uclm.esi.backJuegos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.backJuegos.model.Token;

public interface TokenRepository extends JpaRepository<Token, String> {

}
