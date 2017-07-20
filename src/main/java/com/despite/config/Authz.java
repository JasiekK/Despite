package com.despite.config;

import com.despite.entities.User;
import org.springframework.stereotype.Component;

@Component
public class Authz {

    public boolean check(Long userId, User user) {
        return userId.equals(user.getId());
    }

    public boolean check(String userId, String principal){
        return userId.equals(principal);
    }
}
