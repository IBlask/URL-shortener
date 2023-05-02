package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseRegister register(RequestRegister requestRegister) {
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
}
