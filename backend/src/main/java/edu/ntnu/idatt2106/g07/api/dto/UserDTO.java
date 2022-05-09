package edu.ntnu.idatt2106.g07.api.dto;

import edu.ntnu.idatt2106.g07.api.entity.User;
import lombok.Data;

/**
 * DTO class to transfer user information.
 */
@Data
public class UserDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private int zip;
    private String password;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.phone = user.getPhone();
        this.zip = user.getZip();
        this.password = user.getPassword();
    }

    public UserDTO(String email, String firstname, String lastname, String phone, Integer zip, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.zip = zip;
        this.password = password;
    }
}
