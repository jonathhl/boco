package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.UserRepository;
import edu.ntnu.idatt2106.g07.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Service class for managing Spring Security user details.
 * 
 * @see UserPrincipal
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets the user with the given username.
     * 
     * @param username
     *            Username.
     * 
     * @return User.
     * 
     * @throws UsernameNotFoundException
     *             If user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return UserPrincipal.of(user.get());
    }
}
