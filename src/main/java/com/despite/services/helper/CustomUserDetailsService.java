package com.despite.services.helper;

import com.despite.entities.Role;
import com.despite.entities.User;
import com.despite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("Could not find user :" + username);
        }
    }

    private final static class CustomUserDetails extends User implements UserDetails {

        private Collection<? extends GrantedAuthority> authorities;

        private CustomUserDetails(User user) {
            super(user);
            this.authorities = translate(user.getRoles());
        }

        private Collection<? extends GrantedAuthority> translate(List<Role> roles) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : roles) {
                String name = role.getName().toUpperCase();
                if (!name.startsWith("ROLE_"))
                    name = "ROLE_" + name;
                authorities.add(new SimpleGrantedAuthority(name));
            }
            return authorities;
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
}
