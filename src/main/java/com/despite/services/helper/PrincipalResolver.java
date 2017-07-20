package com.despite.services.helper;

import com.despite.entities.User;
import com.despite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class PrincipalResolver implements IPrincipalResolver{

    private UserRepository userRepository;

    @Autowired
    public PrincipalResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Principal principal) {
        return (userRepository.findByUserName(principal.getName()));
    }
}
