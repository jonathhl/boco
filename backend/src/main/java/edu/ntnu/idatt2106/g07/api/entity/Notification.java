package edu.ntnu.idatt2106.g07.api.entity;

import edu.ntnu.idatt2106.g07.api.dto.NotificationDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    private boolean read;

    @ManyToOne
    @Getter
    @Setter
    private User user;

    @ManyToOne
    @Getter
    @NotNull
    private Chat chat;

    public Notification() {
    }

    public Notification(boolean read, User user, Chat chat) {
        this.read = read;
        this.user = user;
        this.chat = chat;
    }

    public Notification(Notification notification) {
        this.read = notification.read;
        this.user = notification.getUser() != null ? notification.getUser() : null;
        this.chat = notification.getChat();
    }

    public Notification update(NotificationDTO dto) {
        this.read = dto.isRead();

        return this;
    }
}
