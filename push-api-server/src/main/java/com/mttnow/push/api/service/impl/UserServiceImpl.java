package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.User;
import com.mttnow.push.api.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return new User();
        }

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        List<String> roles = new ArrayList<String>();
        for (GrantedAuthority authority : principal.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        User user = new User(principal.getUsername(), roles);

        return user;
    }
}
