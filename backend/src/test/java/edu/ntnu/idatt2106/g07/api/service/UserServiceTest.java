package edu.ntnu.idatt2106.g07.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.g07.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import edu.ntnu.idatt2106.g07.api.entity.User;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.Optional;

@TestComponent
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("User service saves user successfully.")
    void testSaveUser() {
        User expected = new User("test@test.no", "test", "test", "test", 9999, "test");
        when(userRepository.save(expected)).thenReturn(expected);

        User actual = userService.saveUser(expected);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User service hashes password correctly.")
    void testPasswordHashing() {
        String password = "password";
        User expected = new User("test@test.no", "test", "test", "test", 9999, password);
        when(userRepository.save(expected)).thenReturn(expected);

        User actual = userService.saveUser(expected);

        assertNotEquals(password, actual.getPassword());
    }

    @Test
    @DisplayName("User service finds user by id.")
    void testFindById() {
        String email = "email";
        User expected = new User(email, "test", "test", "test", 9999, "test");
        when(userRepository.findById(email)).thenReturn(java.util.Optional.of(expected));

        Optional<User> actual = userService.getUserByEmail(expected.getEmail());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
