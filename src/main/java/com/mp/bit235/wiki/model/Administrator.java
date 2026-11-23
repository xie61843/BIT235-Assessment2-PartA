/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * Administrator is the simple domain object that holds the hard-coded
 * wiki administrator credentials. No database is used in Part A.
 */
package com.mp.bit235.wiki.model;

public class Administrator {

    private final String username;
    private final String password;

    // The constructor is used only by the repository when it creates the single admin record.
    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
