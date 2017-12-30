package com.despite.services.helper;

import com.despite.entities.Role;
import com.despite.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private Collection<? extends GrantedAuthority> authorities;

    CustomUserDetails(User user) {
        this.user = user;
        this.authorities = translate(user.getRoles());
    }

    private Collection<? extends GrantedAuthority> translate(List<Role> role) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (role.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return authorities;
        } else {
            role.forEach(r -> {
                r.setName(r.getName().toUpperCase());
                if (!r.getName().startsWith("ROLE_"))
                    r.setName("ROLE_" + r.getName());
            });
            role.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
            return authorities;
        }
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

