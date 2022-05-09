package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.UserRepository;

import java.util.Optional;

/**
 * Service for managing users.
 * 
 * @see User
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     * 
     * @param user
     *            User to save.
     * 
     * @return New user.
     */
    public User saveUser(User user) {
        String encrypted = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encrypted);
        return userRepository.save(user);
    }

    /**
     * Updates the specified user.
     * 
     * @param userDTO
     *            Data to update and user email.
     * 
     * @return Updated user.
     */
    public Optional<User> updateUserInfo(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getEmail());
        if (optionalUser.isEmpty())
            return Optional.empty();
        User user = optionalUser.get();
        user.update(userDTO);
        try {
            User updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a user by the specified email.
     * 
     * @param email
     *            User email.
     * 
     * @return User with given email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findById(email);
    }
}