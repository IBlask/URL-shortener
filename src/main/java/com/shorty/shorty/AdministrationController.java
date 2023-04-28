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

    //TODO CHECK registracija
    @PostMapping(value = "/administration/register", consumes = "application/json", produces = "application/json")
    //@GetMapping("/administration/register")
    public ResponseRegister register (@RequestBody RequestRegister requestRegister) {
        ResponseRegister responseRegister = new ResponseRegister();

        //je li poslan prazan POST
        if (requestRegister.getAccountID() == null) {
            return responseRegister;
        }
        //je li poslan accountID
        else if (requestRegister.getAccountID() == "") {
            responseRegister.setDescription("Please enter your username!");
        }

        //provjeriti postoji li accountID
        else if (requestRegister.accountIdExists(userRepository)) {
            //ako postoji, error
            responseRegister.setDescription("Account ID already exists!");

        }
        //ako ne postoji, generiraj lozinku i spremi podatke u bazu
        else {
            responseRegister.generatePassword();
            responseRegister.setDescription(null);
            User newUser = new User();
            newUser.setUsername(requestRegister.getAccountID());
            newUser.setPassword(responseRegister.getPassword());
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
