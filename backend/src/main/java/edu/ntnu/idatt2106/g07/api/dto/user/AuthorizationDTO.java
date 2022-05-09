package edu.ntnu.idatt2106.g07.api.dto.user;

import lombok.Data;

@Data
public class AuthorizationDTO {
    private String username;
    private String password;

    public AuthorizationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
