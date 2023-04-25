package com.shorty.shorty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import responses.ResponseLogin;
import responses.ResponseRegister;
import responses.ResponseShort;
import responses.ResponseStatistics;

@RestController
public class AdministrationController {

    //TODO registracija
    @GetMapping("/administration/register")
    public ResponseRegister register () {
        //test
        ResponseRegister ret1 = new ResponseRegister();
        return ret1;
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
