package com.shorty.shorty.dto.request;

import com.shorty.shorty.repository.UserRepository;

public class RequestRegister {

    private String accountID;

    public RequestRegister() {
        super();
    }

    public RequestRegister(String accountID) {
        this.accountID = accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public boolean isEmpty() {
        return accountID == null;
    }

    public boolean accountIdIsBlank() {
        return accountID.isBlank();
    }

    public boolean accountIdExists(UserRepository userRepository) {
        return !userRepository.findByUsername(this.accountID).isEmpty();
    }
}
