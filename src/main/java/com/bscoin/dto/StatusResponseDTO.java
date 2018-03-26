package com.bscoin.dto;

public class StatusResponseDTO {

	private String status;
	private String message;
	private LoginDTO loginInfo;

	public LoginDTO getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginDTO loginInfo) {
		this.loginInfo = loginInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}