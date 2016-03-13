package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.LoginDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LoginControllerTest {
    LoginController controller;
    @Before
    public void setUp() throws Exception {
        controller = new LoginController();
    }

    @Test
    public void testSubmit() throws Exception {
        LoginDetails expected = new LoginDetails();
        String username = "admin";
        String password = "adminadmin";
        expected.setUsername(username);
        expected.setPassword(password);
        LoginDetails actual = controller.submit(username, password);
        assertTrue(expected.equals(actual));
    }
}
