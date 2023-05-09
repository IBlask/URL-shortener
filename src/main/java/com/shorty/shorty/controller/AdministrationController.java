package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.dto.response.ResponseStatistics;
import com.shorty.shorty.repository.UserRepository;
import com.shorty.shorty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministrationController {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @PostMapping(value = "/administration/register", consumes = "application/json", produces = "application/json")
    public ResponseRegister register (@RequestBody RequestRegister requestRegister) {
        return userService.register(requestRegister, userRepository);
    }



    //TODO prijava
    @GetMapping("/administration/login")
    public ResponseLogin login () {

        return null;
    }



    //TODO short
    @GetMapping("/administration/short")
    public ResponseShort shortIt () {

        return null;
    }



    //TODO statistics
    @GetMapping("/administration/statistics")
    public ResponseStatistics statistics () {

        return null;
    }

}
