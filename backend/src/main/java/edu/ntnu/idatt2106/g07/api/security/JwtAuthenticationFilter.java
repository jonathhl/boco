package edu.ntnu.idatt2106.g07.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security class dedicated to the JWT filter.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Method to validate a user token in the http header.
     * 
     * @param request
     *            asks for the user token.
     * @param response
     *            sends the token.
     * @param filterChain
     *            sets the filter on the request and response.
     * 
     * @throws ServletException
     *             is thrown if the servlet encounters any difficulty.
     * @throws IOException
     *             is thrown if the I/O fails or is interrupted.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);

            if (token != null) {
                if (!tokenProvider.validateToken(token)) {
                    throw new IllegalStateException("Unable to validate token");
                }

                String username = tokenProvider.getUsernameFromToken(token);
                UserDetails userdetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userdetails, null,
                        userdetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Get-method to retrieve token from request.
     * 
     * @param request
     *            receives the http request.
     * 
     * @return a substring with the token.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer == null || bearer.isBlank() || !bearer.startsWith("Bearer ")) {
            return null;
        }

        return bearer.substring(7);
    }
}
