package edu.uclm.esi.backJuegos.model;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.backJuegos.http.Client;
import edu.uclm.esi.backJuegos.http.Manager;

public class Email {
	
	public void send(String destinatario, String subject, String body) {
		JSONObject jEmail = Manager.get().getConfiguration().getJSONObject("email");
		
		JSONArray jsaHeaders = new JSONArray().
				put("api-key").put(jEmail.getString("api-key")).
				put("accept").put("application/json").
				put("content-type").put("application/json");
		
		JSONObject jsoTo = new JSONObject().
				put("email", destinatario).
				put("name", destinatario);
		
		JSONObject jsoData = new JSONObject().
				put("sender", jEmail.getJSONObject("sender")).
				put("to", new JSONArray().put(jsoTo)).
				put("subject", subject).
				put("htmlContent", body);
		
		JSONObject payload = new JSONObject().
			put("url", jEmail.getString("endpoint")).
			put("headers", jsaHeaders).
			put("data", jsoData);

		Client client = new Client();
		client.sendCurlPost(payload, body);
    }
	
	public static void main(String[] args) throws Exception {
		Email sender=new Email();
		sender.send("macario.polo@uclm.es", "Hola", "Caracola");
		System.out.println("Enviado");
	}
}
