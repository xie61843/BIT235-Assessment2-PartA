/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * AuthenticationResult is returned by the service layer so that the
 * controller never has to interpret a bare boolean. It carries the outcome,
 * a user-facing message and the display name of the authenticated admin.
 */
package com.mp.bit235.wiki.model;

public class AuthenticationResult {

    private final boolean success;
    private final String message;
    private final String displayName;

    private AuthenticationResult(boolean success, String message, String displayName) {
        this.success = success;
        this.message = message;
        this.displayName = displayName;
    }

    // Factory method used when the supplied credentials match the hard-coded admin.
    public static AuthenticationResult success(String displayName) {
        return new AuthenticationResult(true, "Login successful.", displayName);
    }

    // Factory method used when validation or the credential check fails.
    public static AuthenticationResult failure(String message) {
        return new AuthenticationResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getDisplayName() {
        return displayName;
    }
}
