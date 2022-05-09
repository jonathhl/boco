package edu.ntnu.idatt2106.g07.api.misc;

import java.time.LocalDateTime;

/**
 * Attachable classes can be inserted into chat messages.
 * 
 * @see edu.ntnu.idatt2106.g07.api.dto.chat.RichChatMessageDTO
 */
public interface Attachable {
    LocalDateTime getTime();

    String getFrom();
}
