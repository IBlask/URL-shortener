package com.shorty.shorty.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.RandomStringUtils;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseRegister {
    private boolean success = false;
    private String description = "Error occurred! Please try again.";
    private String password;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void generatePassword() {
        this.password = RandomStringUtils.random(8, true, true);
    }
}
