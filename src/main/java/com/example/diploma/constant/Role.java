package com.example.diploma.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum Role {
	USER("Пользователь"),
	ADMIN("Администратор");
	private final String role;

	Role(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return role;
	}
}
