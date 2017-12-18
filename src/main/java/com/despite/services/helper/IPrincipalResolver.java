package com.despite.services.helper;

import com.despite.entities.User;

import java.security.Principal;

public interface IPrincipalResolver {
    User getUser(Principal principal);
}
