package edu.ntnu.idatt2106.g07.api.dto.chat;

import edu.ntnu.idatt2106.g07.api.entity.ChatMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageChatMessageDTO extends ChatMessageDTO {
    private LocalDateTime time;
    private String from;
    private String message;

    public MessageChatMessageDTO(ChatMessage message) {
        this.type = ChatType.MESSAGE;
        this.time = message.getTime();
        this.from = message.getSender();
        this.message = message.getMessage();
    }
}
