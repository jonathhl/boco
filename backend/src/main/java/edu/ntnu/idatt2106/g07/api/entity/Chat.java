package edu.ntnu.idatt2106.g07.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @ManyToMany
    @JoinColumn(name = "user_email")
    @NotNull
    @Getter
    private List<User> users;

    @OneToMany(mappedBy = "chat")
    @Getter
    @Setter
    private List<ChatMessage> messages = new ArrayList<>();

    public Chat() {
    }

    public Chat(List<User> users) {
        this.users = users;
    }
}
