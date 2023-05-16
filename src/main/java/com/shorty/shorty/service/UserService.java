package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public ResponseRegister register(RequestRegister requestRegister, UserRepository userRepository) {
        ResponseRegister responseRegister = new ResponseRegister();

        //is POST request empty
        if (requestRegister.isEmpty()) {
            return responseRegister;
        }
        //was accountID entered/sent
        else if (requestRegister.accountIdIsBlank()) {
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


    public ResponseLogin login(RequestLogin requestLogin, UserRepository userRepository) {
        ResponseLogin responseLogin = new ResponseLogin();

        //is POST request empty or is any parameter missing
        if (requestLogin.isEmpty() || requestLogin.accountIdIsBlank() || requestLogin.passwordIsBlank()) {
            return responseLogin;
        }
        //are accountID and password in the database / is user registered
        else if (requestLogin.accountIdAndPasswordAreMatching(userRepository)) {
            responseLogin.setSuccess(true);
        }

        return responseLogin;
    }
}
