package com.shorty.shorty.dto.request;

import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UserRepository;

public class RequestLogin {
    private String accountID;
    private String password;

    public RequestLogin() {
        super();
    }

    public RequestLogin(String accountID, String password) {
        this.accountID = accountID;
        this.password = password;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmpty() {
        return accountID == null || password == null;
    }

    public boolean accountIdIsBlank() {
        return accountID.isBlank();
    }

    public boolean passwordIsBlank() {
        return password.isBlank();
    }

    public boolean accountIdAndPasswordAreMatching(UserRepository userRepository) {
        User user = userRepository.findByUsername(this.accountID);

        if ((user != null) && user.getPassword().equals(this.password)) {
            return true;
        }

        return false;
    }
}
