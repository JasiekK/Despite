package com.despite.services.helper;

import com.despite.entities.User;
import com.despite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
