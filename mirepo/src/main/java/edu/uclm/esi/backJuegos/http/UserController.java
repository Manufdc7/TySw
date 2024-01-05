package edu.uclm.esi.backJuegos.http;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.backJuegos.model.User;
import edu.uclm.esi.backJuegos.services.UserService;
import edu.uclm.esi.backJuegos.webdto.LoginDto;
import edu.uclm.esi.backJuegos.webdto.ResultDto;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController extends CookiesController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/login")
	public ResponseEntity<LoginDto> login(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		LoginDto loginDto = new LoginDto();
		String name = jso.getString("name");
		String pwd = jso.getString("pwd");
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		String ip = request.getRemoteAddr();
		User user = Manager.get().getUsersRepository().findByNameAndPwd(name, pwd);
		if (user != null) { // || user.getConfirmationDate()==null)
			Cookie cookie = readOrCreateCookie(request, response);
			userService.insertLogin(user, ip, cookie);
			request.getSession().setAttribute("userId", user.getId());
			response.addCookie(cookie);
			session.setAttribute("userId", user.getId());
			HttpSession existingSession = Manager.get().findHttpSession(user.getId());
			if (existingSession != null) {
				existingSession.invalidate();
				Manager.get().removeHttpSession(user.getId());
				Manager.get().removeSessionByUserId(user.getId());
			}
			Manager.get().addHttpSession(user.getId(), session);
			Manager.get().addSessionByUserId(user.getId(), session);
			loginDto.setUserId(user.getId());
			loginDto.setHttpSessionId(session.getId());
			loginDto.setUserName(user.getName());
		}
			
		return ResponseEntity.ok(loginDto);
	}

	@PostMapping("/register")
	public ResponseEntity<ResultDto> register(@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		ResultDto result = new ResultDto();
		result.setResult("OK");
		String userName = jso.optString("username");
		String email = jso.optString("email");
		String pwd1 = jso.optString("pwd1");
		String pwd2 = jso.optString("pwd2");
		if (!pwd1.equals(pwd2)) {
			result.setResult("KO");
			result.setMessage("Error: las contraseñas no coinciden");
		}
		if (pwd1.length() < 4) {
			result.setResult("KO");
			result.setMessage("Error: la contraseña debe tener al menos cuatro caracteres");
		}

		try {
			if(result.getResult().equals("OK")) {
				User user = new User();
				user.setName(userName);
				user.setEmail(email);
				user.setPwd(pwd1);
				userService.save(user);
				result.setMessage("Revisa tu correo para completar el registro");
			}
		} catch (ResponseStatusException e) {
			result.setResult("KO");
			result.setMessage("Error: Ha habido algún problema con el registro");
		} catch (IOException e) {
			result.setResult("KO");
			result.setMessage("Error: Ha habido algún problema con el registro");
		} catch (Exception e) {
			result.setResult("KO");
			result.setMessage("Error: Ese correo o nombre de usuario ya está en uso");
		}
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/remove/{userId}")
	public void remove(@PathVariable String userId) {
		System.out.println("Borrar el usuario con id " + userId);
	}

	@GetMapping("/validateAccount/{tokenId}")
	public void validateAccount(HttpServletResponse response, @PathVariable String tokenId) {
		userService.validateToken(tokenId);
		// Ir a la base de datos, buscar el token con ese tokenId en la tabla, ver que
		// no ha caducado
		// y actualizar la confirmationDate del user
		try {
			response.sendRedirect(Manager.get().getConfiguration().getString("home"));
		} catch (IOException e) {

		}
	}

	@GetMapping("/recoverPwd")
	public ResponseEntity<ResultDto> recoverPwd(@RequestParam String email) {

		ResultDto result = new ResultDto();
		result.setResult("OK");

		try {
			userService.recoverPwd(email);
			result.setMessage("Te hemos enviado tu contraseña al correo con el que estás registrado en pideamesa.com");
		} catch (ResponseStatusException e) {
			result.setResult("KO");
			result.setMessage("Error: Ha habido algún problema");
		} catch (IOException e) {
			result.setResult("KO");
			result.setMessage("Error: Ha habido algún problema con el registro");
		}
		return ResponseEntity.ok(result);

	}

	@GetMapping("/recoverPwd2/{tokenId}")
	public void recoverPwd2(HttpServletResponse response, @PathVariable String tokenId) {
		// Ir a la base de datos, buscar el token con ese tokenId en la tabla, ver que
		// no ha caducado
		// y pasar a enseñar la contraseña
		String url = userService.showPwd(tokenId);
		// System.out.println(tokenId);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// response.sendRedirect("error.html");
			e.printStackTrace();
		}
	}

	@PostMapping("/updatePwd")
	public ResponseEntity<ResultDto> updatePwd(@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		ResultDto result = new ResultDto();
		result.setResult("OK");
		String pwd1 = jso.optString("pwd1");
		String pwd2 = jso.optString("pwd2");
		String token = jso.optString("tokenId");
		if (!pwd1.equals(pwd2)) {
			result.setResult("KO");
			result.setMessage("Error: Las contraseñas deben ser iguales");
		}
		if (pwd1.length() < 4) {
			result.setResult("KO");
			result.setMessage("Error: la contraseña debe tener al menos cuatro caracteres");
		}
		try {
			if (result.getResult().equals("OK")) {
				userService.updatePwd(pwd1, token);
				result.setResult("OK");
				result.setMessage("Contraseña actualizada con éxito");
			}
		} catch (Exception e) {
			throw e;
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
