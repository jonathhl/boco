package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.security.JwtProvider;
import edu.ntnu.idatt2106.g07.api.security.UserPrincipal;
import edu.ntnu.idatt2106.g07.api.service.AuthorizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {
    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtProvider tokenProvider;

    @Autowired
    @InjectMocks
    private AuthorizationService authorizationService;

    private final JwtProvider jwtProvider = new JwtProvider();

    @Test
    @DisplayName("Authorization endpoint returns JWT with correct username.")
    void testAuthorizationEndpointReturnsTokenWithCorrectUsername() {
        String username = "user";
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(username, "");

        Authentication auth = new UsernamePasswordAuthenticationToken(new UserPrincipal(null, username, ""), null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(authManager.authenticate(credentials)).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn(jwtProvider.generateToken(auth));

        String token = authorizationService.getTokenFromCredentials(credentials);

        assertEquals(username, jwtProvider.getUsernameFromToken(token));
    }
}
