package edu.ntnu.idatt2106.g07.api.security;

import edu.ntnu.idatt2106.g07.api.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Class dedicated to the user principal details.
 */
public class UserPrincipal implements UserDetails {
    private Collection<GrantedAuthority> authorities;
    private String username;
    private String password;

    public UserPrincipal() {
    }

    public UserPrincipal(Collection<GrantedAuthority> authorities, String username, String password) {
        this.authorities = authorities;
        this.username = username;
        this.password = password;
    }

    /**
     * Takes the user and defines the roles and authorities.
     * 
     * @param user
     *            retrieves the user details.
     * 
     * @return a principal.
     */
    public static UserPrincipal of(User user) {
        UserPrincipal principal = new UserPrincipal();

        principal.username = user.getEmail();
        principal.password = user.getPassword();
        principal.authorities = List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", user.getRole())));

        return principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
