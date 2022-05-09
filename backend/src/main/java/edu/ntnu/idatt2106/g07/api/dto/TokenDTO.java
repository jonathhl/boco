package edu.ntnu.idatt2106.g07.api.dto;

import lombok.Data;

/**
 * DTO class to transfer token information.
 */
@Data
public class TokenDTO {
    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
