package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for managing authorization.
 */
@Service
public class AuthorizationService {
    private final AuthenticationManager authManager;
    private final JwtProvider tokenProvider;

    @Autowired
    public AuthorizationService(AuthenticationManager authManager, JwtProvider tokenProvider) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Gets a token from given credentials.
     * 
     * @param token
     *            Credentials.
     * 
     * @return JWT.
     */
    public String getTokenFromCredentials(UsernamePasswordAuthenticationToken token) {
        Authentication auth = authManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return tokenProvider.generateToken(auth);
    }
}
