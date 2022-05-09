package edu.ntnu.idatt2106.g07.api.dto.chat;

import lombok.Data;

@Data
public abstract class ChatMessageDTO {
    protected ChatType type;
}

enum ChatType {
    MESSAGE, REQUEST, RATING;
}