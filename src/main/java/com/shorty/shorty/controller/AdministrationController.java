package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
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

import java.util.List;

@RestController
public class AdministrationController {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @PostMapping(value = "/administration/register", consumes = "application/json", produces = "application/json")
    public ResponseRegister register (@RequestBody RequestRegister requestRegister) {
        return userService.register(requestRegister, userRepository);
    }



    @PostMapping(value = "/administration/login", consumes = "application/json", produces = "application/json")
    public ResponseLogin login (@RequestBody RequestLogin requestLogin) {
        return userService.login(requestLogin, userRepository);
    }



    @Autowired
    ShortingService shortingService;
    @Autowired
    UrlRepository urlRepository;

    //TODO short
    @GetMapping("/administration/short")
    public ResponseShort shortIt () {

        return null;
    }



    //TODO CHECK statistics
    @GetMapping("/administration/statistics")
    public List<ResponseStatistics> statistics (@RequestHeader(name = "Authorization", required = false) String authToken) {
        return shortingService.getStatistics(authToken, userRepository, urlRepository);
    }

}
