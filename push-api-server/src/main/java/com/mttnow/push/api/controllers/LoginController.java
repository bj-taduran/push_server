package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.LoginDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(value = {"/submit"}, method = RequestMethod.GET)
    public @ResponseBody
    LoginDetails submit(@RequestParam("uname") String username, @RequestParam("password") String password) {
        LoginDetails details = new LoginDetails();
        details.setUsername(username);
        details.setPassword(password);
        return details;
    }
}


