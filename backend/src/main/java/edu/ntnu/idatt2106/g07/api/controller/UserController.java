package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.ntnu.idatt2106.g07.api.dto.user.UserDTO;
import edu.ntnu.idatt2106.g07.api.service.UserService;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for managing users.
 * 
 * @see User
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     * 
     * @param data
     *            User to create.
     * 
     * @return New user.
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDTO data) {
        return ResponseEntity.ok(new UserDTO(userService.saveUser(new User(data))));
    }

    /**
     * Gets the active users information.
     * 
     * @param principal
     *            Active user.
     * 
     * @return User.
     */
    @GetMapping
    public ResponseEntity<Object> getCurrentUser(Principal principal) {
        Optional<User> optionalUser = userService.getUserByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        return ResponseEntity.ok(new UserDTO(optionalUser.get()));
    }

    /**
     * Updates the active user.
     * 
     * @param data
     *            Data to update.
     * @param principal
     *            Active user.
     * 
     * @return Updated user.
     */
    @PutMapping
    public ResponseEntity<Object> updateUserInfo(@RequestBody UserDTO data, Principal principal) {
        if (!Objects.equals(data.getEmail(), principal.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO("No permission to edit this user information."));
        }

        if (!data.getPassword().equals(""))
            data.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));

        Optional<User> optionalUser = userService.updateUserInfo(data);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        return ResponseEntity.ok(new UserDTO(optionalUser.get()));
    }
}