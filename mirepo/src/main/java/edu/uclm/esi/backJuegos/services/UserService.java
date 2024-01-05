package edu.uclm.esi.backJuegos.services;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.backJuegos.dao.LoginRepository;
import edu.uclm.esi.backJuegos.http.Manager;
import edu.uclm.esi.backJuegos.model.Email;
import edu.uclm.esi.backJuegos.model.Login;
import edu.uclm.esi.backJuegos.model.Token;
import edu.uclm.esi.backJuegos.model.User;

@Service
public class UserService {
	@Autowired
	private LoginRepository loginDAO;
	
	public void save(User user) throws IOException, SQLIntegrityConstraintViolationException {
		Manager.get().getUsersRepository().save(user);
		Token token = new Token(user.getEmail());
		Manager.get().getTokensRepository().save(token);
		Email smtp=new Email();
		String body = Manager.get().readTextFile("confirmarRegistro.html.txt");
		body = body.replace("#URL_CONFIRMACION_REGISTRO#", Manager.get().getConfiguration().getString("urlConfirmacion") + token.getId());
		smtp.send(user.getEmail(), "Bienvenido al sistema", body);
	}

	public void validateToken(String tokenId) {
		Optional<Token> optToken = Manager.get().getTokensRepository().findById(tokenId);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			long date = token.getDate();
			long now = System.currentTimeMillis();
			if (now>date+24*60*60*1000)
				throw new ResponseStatusException(HttpStatus.GONE, "Token caducado");
			String email = token.getEmail();
			User user = Manager.get().getUsersRepository().findByEmail(email);
			if (user!=null) {
				user.setConfirmationDate(now);
				Manager.get().getUsersRepository().save(user);
			} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token " + tokenId + " no encontrado");
	}

	public void insertLogin(User user, String ip, Cookie cookie) {
		Login login = new Login();
		login.setEmail(user.getEmail());
		login.setDate(System.currentTimeMillis());
		login.setIp(ip);
		login.setCookieValue(cookie.getValue());
		loginDAO.save(login);
	}

	public void recoverPwd(String email) throws IOException {
		// TODO Auto-generated method stub
		/*buscar usurio
		si no existe lanzar excepcion
		si existe, generarle un token, y mandarle un email con un link donde recuperará la PWD*/
		Optional<User> optUser = Optional.ofNullable(Manager.get().getUsersRepository().findByEmail(email));
		if (optUser.isPresent()) {
			User user = optUser.get();
			Email smtp=new Email();
			boolean versionCorta=false;
			if (versionCorta) {
				String body = Manager.get().readTextFile("recuperarPWD.html.txt");
				body = body.replace("#PASSWORD#", user.getPwd());
				smtp.send(user.getEmail(), "Recuperacion de contraseña", body);
			}
			else
			{
				Token token= new Token(email);
				Manager.get().getTokensRepository().save(token);
				String body = Manager.get().readTextFile("recuperarPWD2.html.txt");
				body = body.replace("#URL_RECUPERAR_PWD#", Manager.get().getConfiguration().getString("urlRecover") + token.getId());
				smtp.send(user.getEmail(), "Recuperacion de contraseña", body);
			}
		} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	}

	public String showPwd(String tokenId) {
		String url;
		Optional<Token> optToken = Manager.get().getTokensRepository().findById(tokenId);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			long date = token.getDate();
			long now = System.currentTimeMillis();
			if (now>date+24*60*60*1000)
				throw new ResponseStatusException(HttpStatus.GONE, "Enlace caducado");
			String email = token.getEmail();
			User user = Manager.get().getUsersRepository().findByEmail(email);
			if (user!=null) {
				//response.sendRedirect(Manager.get().getConfiguration().getString("home"));
				
				
				url = Manager.get().getConfiguration().getString("resetPwd");	
				url = url + tokenId;
			} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token " + tokenId + " no encontrado");
		return url;
	}

	public void updatePwd(String pwd, String tokenId) {
		// TODO Auto-generated method stub
		Optional<Token> optToken = Manager.get().getTokensRepository().findById(tokenId);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			String email = token.getEmail();
			User user = Manager.get().getUsersRepository().findByEmail(email);
			if (user!=null) {
				user.setPwd(pwd);
				Manager.get().getUsersRepository().save(user);
			}
			else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		} else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token " + tokenId + " no encontrado");
	}
}
