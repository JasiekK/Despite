package com.despite.config.helper;

import com.despite.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    public SecurityContext createSecurityContext(WithCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User userFromPrincipal = new User();
        userFromPrincipal.setUsername(customUser.userName());
        userFromPrincipal.setId(customUser.id());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(userFromPrincipal,
                        "user",
                        AuthorityUtils.createAuthorityList("ROLE_USER"));

        context.setAuthentication(auth);
        return context;
    }
}
