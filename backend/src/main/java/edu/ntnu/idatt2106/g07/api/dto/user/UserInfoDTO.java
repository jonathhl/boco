package edu.ntnu.idatt2106.g07.api.dto.user;

import edu.ntnu.idatt2106.g07.api.entity.User;
import lombok.Data;

@Data
public class UserInfoDTO {
    private String email;
    private String firstname;
    private String lastname;

    public UserInfoDTO(User user) {
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
    }
}
