package edu.uclm.esi.backJuegos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.backJuegos.model.Login;

public interface LoginRepository extends JpaRepository <Login, String> {

}
