package edu.ntnu.idatt2106.g07.api.dto;

import edu.ntnu.idatt2106.g07.api.entity.Notification;
import lombok.Data;

@Data
public class NotificationDTO {

    private long id;

    private boolean read;

    private String email;

    private Long chat_id;

    public NotificationDTO(boolean read, String email, Long chat_id) {
        this.read = read;
        this.email = email;
        this.chat_id = chat_id;
    }

    public NotificationDTO() {
    }

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.read = notification.isRead();
        this.email = notification.getUser() != null ? notification.getUser().getEmail() : null;
        this.chat_id = notification.getChat().getId();
    }
}
