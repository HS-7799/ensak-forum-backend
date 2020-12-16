package com.forum.ensak.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "Username is required field.")
	private String username;

	@NotBlank(message = "Password is a required field.")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
