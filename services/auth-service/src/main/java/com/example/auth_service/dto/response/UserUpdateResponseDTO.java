package com.example.auth_service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateResponseDTO extends UserResponseDTO {

	private String username;

	public UserUpdateResponseDTO(String username, String firstName, String lastName, boolean active) {
        super(firstName, lastName , active);
		this.username = username;
    }

}
