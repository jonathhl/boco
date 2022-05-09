package edu.ntnu.idatt2106.g07.api.dto.user;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
