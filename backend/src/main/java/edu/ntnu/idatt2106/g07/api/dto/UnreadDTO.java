package edu.ntnu.idatt2106.g07.api.dto;

import lombok.Data;

@Data
public class UnreadDTO {
    private boolean unread;

    public UnreadDTO(boolean unread) {
        this.unread = unread;
    }
}
