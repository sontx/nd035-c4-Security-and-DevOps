package com.example.demo.model.requests;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {
	@Size(min = 4)
	private String username;

	@Size(min = 4)
	private String password;

	@Size(min = 4)
	private String confirmPassword;
}
