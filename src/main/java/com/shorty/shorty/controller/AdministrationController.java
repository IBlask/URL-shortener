package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.dto.response.ResponseStatistics;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import com.shorty.shorty.service.ShortingService;
import com.shorty.shorty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



    @Autowired
    ShortingService shortingService;
    @Autowired
    UrlRepository urlRepository;

    //TODO CHECK short
    @PostMapping(value = "/administration/short", consumes = "application/json", produces = "application/json")
    public ResponseShort shorting (@RequestHeader(name = "Authorization", required = false) String authToken, @RequestBody RequestShort requestShort) {
        return shortingService.shortenUrl(requestShort, urlRepository, authToken, userRepository);
    }



    //TODO statistics
    @GetMapping("/administration/statistics")
    public ResponseStatistics statistics () {

        return null;
    }

}
