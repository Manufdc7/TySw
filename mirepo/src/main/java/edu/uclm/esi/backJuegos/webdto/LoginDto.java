package edu.uclm.esi.backJuegos.webdto;

public class LoginDto {
	
	private String userId;
	private String httpSessionId;
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHttpSessionId() {
		return httpSessionId;
	}
	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

}
