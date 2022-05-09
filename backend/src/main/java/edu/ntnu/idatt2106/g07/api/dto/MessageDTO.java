package edu.ntnu.idatt2106.g07.api.dto;

import lombok.Data;

@Data
public class MessageDTO {

    private String message;

    public MessageDTO() {
    }

    public MessageDTO(String message) {
        this.message = message;
    }
}
