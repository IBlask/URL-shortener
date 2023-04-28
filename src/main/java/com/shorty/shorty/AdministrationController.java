package com.shorty.shorty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import requests.RequestRegister;
import responses.ResponseLogin;
import responses.ResponseRegister;
import responses.ResponseShort;
import responses.ResponseStatistics;

@RestController
public class AdministrationController {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/administration/register", consumes = "application/json", produces = "application/json")
    public ResponseRegister register (@RequestBody RequestRegister requestRegister) {
        ResponseRegister responseRegister = new ResponseRegister();

        //is POST request empty
        if (requestRegister.isEmpty()) {
            return responseRegister;
        }
        //was accountID entered/sent
        else if (requestRegister.accountIdIsNotSent()) {
            responseRegister.setDescription("Please enter your username!");
        }

        //does accountID exist in database
        else if (requestRegister.accountIdExists(userRepository)) {
            responseRegister.setDescription("Account ID already exists!");

        }
        //if accountID doesn't exist, generate password and insert data to database
        else {
            responseRegister.generatePassword();
            responseRegister.setDescription(null);
            responseRegister.setSuccess(true);
            User newUser = new User(requestRegister.getAccountID(), responseRegister.getPassword());
            userRepository.save(newUser);
        }

        return responseRegister;
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
