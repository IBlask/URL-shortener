package responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
        String alNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                     + "abcdefghijklmnopqrstuvwxyz"
                     + "0123456789";

        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            pass.append(alNum.charAt((int)(Math.random() * alNum.length())));
        }
        this.password = pass.toString();
    }
}
