package com.mttnow.push.api.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.mttnow.push.api.models.User;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
public class UserServiceImplTest {
  
  UserServiceImpl userService;
  
  @Before
  public void setUp(){
    PowerMockito.mockStatic(SecurityContextHolder.class);
    userService = new UserServiceImpl();
  }

  @Test
  public void shouldReturnUserWithRolesWhenAuthenticated(){
    SecurityContext securityContext = new SecurityContextImpl();
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    Authentication authentication = new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User("username", "password", authorities), null);
    securityContext.setAuthentication(authentication);
    
    when(SecurityContextHolder.getContext()).thenReturn(securityContext);
    User user = userService.getCurrentUser();
    assertEquals("username", user.getUsername());
    assertEquals("ROLE_ADMIN", user.getRoles().get(0));
  }
  
  @Test
  public void shouldReturnEmptyUserWhenNotAuthenticated(){
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(null);
    
    when(SecurityContextHolder.getContext()).thenReturn(securityContext);
    User user = userService.getCurrentUser();
    assertNull(null, user.getUsername());
    assertNull(null, user.getRoles());
  }
  
}
