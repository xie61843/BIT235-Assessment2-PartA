/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * LoginForm is the Model object that carries the data submitted by the
 * login page through to the controller and the service layer.
 */
package com.mp.bit235.wiki.model;

import javax.validation.constraints.NotBlank;

public class LoginForm {

    // Both fields are mandatory; the messages are displayed on the error page.
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;

    public String getUsername() {
        return username;
    }

    // The setter receives the value typed into the username input.
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // The setter receives the value typed into the password input.
    public void setPassword(String password) {
        this.password = password;
    }
}
