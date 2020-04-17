package com.tismenetski.ppmtool.payload;


import javax.validation.constraints.NotBlank;

//A class to handle the login request - validating the fields of username and password ->Used in the controller as a Request body for validation
public class LoginRequest {

    @NotBlank(message = "Username  cannot be blank")
    private String username;
    @NotBlank(message = "Password  cannot be blank")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
