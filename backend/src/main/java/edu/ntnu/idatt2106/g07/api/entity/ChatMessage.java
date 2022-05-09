package edu.ntnu.idatt2106.g07.api.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @Getter
    private Chat chat;

    @NotNull
    @Getter
    private String sender;

    @NotNull
    @Getter
    private String message;

    @NotNull
    @Getter
    private LocalDateTime time;

    public ChatMessage() {
    }

    @PrePersist
    void time() {
        this.time = LocalDateTime.now();
    }

    public ChatMessage(Chat chat, String message, String sender) {
        this.chat = chat;
        this.message = message;
        this.sender = sender;
    }
}