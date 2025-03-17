package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateResponseDTO extends UserResponseDTO {

	private String username;

	public UserUpdateResponseDTO(String username, String firstName, String lastName, Boolean active) {
        super(firstName, lastName , active);
		this.username = username;
    }

}
