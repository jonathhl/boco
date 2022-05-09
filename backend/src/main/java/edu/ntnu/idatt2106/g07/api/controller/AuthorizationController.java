package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.user.AuthorizationDTO;
import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.dto.user.TokenDTO;
import edu.ntnu.idatt2106.g07.api.service.AuthorizationService;
import edu.ntnu.idatt2106.g07.api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Controller for authorizing user and retrieving token.
 * 
 * @see JwtAuthenticationFilter
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authService;

    @Autowired
    public AuthorizationController(AuthorizationService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates the user and returns a JWT.
     * 
     * @param data
     *            Credentials.
     * 
     * @return JWT token for authorization.
     */
    @PostMapping
    public ResponseEntity<Object> getTokenFromCredentials(@Valid @RequestBody AuthorizationDTO data) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(data.getUsername(),
                data.getPassword());
        String token = authService.getTokenFromCredentials(credentials);

        return ResponseEntity.ok(new TokenDTO(token));
    }

    /**
     * Verifies the user is authenticated for testing purposes.
     * 
     * @param principal
     *            Active user.
     * 
     * @return Confirmation message.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getAuthConfirmation(Principal principal) {
        String message = String.format("Successfully authenticated as %s.", principal.getName());

        return ResponseEntity.ok(new MessageDTO(message));
    }
}
