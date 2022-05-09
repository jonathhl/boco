package edu.ntnu.idatt2106.g07.api.entity;

import edu.ntnu.idatt2106.g07.api.dto.user.UserDTO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class User {
    @Id
    @Getter
    private String email;

    @NotNull
    @Getter
    @Setter
    private String firstname;

    @Getter
    @Setter
    private String lastname;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private int zip;

    @Getter
    @Setter
    private String password;

    @ManyToMany(mappedBy = "users")
    @Getter
    private List<Chat> chats;

    @OneToMany(mappedBy = "user")
    @Getter
    private List<Listing> listings;

    @Getter
    @Setter
    private Role role = Role.USER;

    public User(String email, String firstname, String lastname, String phone, int zip, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.zip = zip;
        this.password = password;
    }

    public User(UserDTO data) {
        this.email = data.getEmail();
        this.firstname = data.getFirstname();
        this.lastname = data.getLastname();
        this.phone = data.getPhone();
        this.zip = data.getZip();
        this.password = data.getPassword();
    }

    public User() {
    }

    public User update(UserDTO dto) {
        if (dto.getEmail() != null)
            this.email = dto.getEmail();
        if (dto.getFirstname() != null)
            this.firstname = dto.getFirstname();
        if (dto.getLastname() != null)
            this.lastname = dto.getLastname();
        if (dto.getPhone() != null)
            this.phone = dto.getPhone();
        if (dto.getZip() != 0)
            this.zip = dto.getZip();
        if (!dto.getPassword().equals(""))
            this.password = dto.getPassword();

        return this;
    }

    enum Role {
        USER, ADMIN
    }
}