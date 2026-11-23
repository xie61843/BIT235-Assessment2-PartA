/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * AuthenticationService owns the business rules for the login: it validates
 * the submitted values, asks the repository for the administrator record and
 * compares the password. Keeping these rules out of the controller is what
 * separates the service layer from the web layer.
 */
package com.mp.bit235.wiki.service;

import com.mp.bit235.wiki.model.Administrator;
import com.mp.bit235.wiki.model.AuthenticationResult;
import com.mp.bit235.wiki.repository.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service // Registers this class as a service-layer bean.
public class AuthenticationService {

    private final AdministratorRepository administratorRepository;

    // Constructor injection makes the dependency explicit and keeps the service testable.
    public AuthenticationService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    /**
     * Validates the submitted credentials and returns a result object that the
     * controller can render without knowing any of the authentication rules.
     *
     * @param username the raw username from the form
     * @param password the raw password from the form
     * @return a success result carrying the display name, or a failure result
     *         carrying a message for the error page
     */
    public AuthenticationResult authenticate(String username, String password) {

        // Rule 1: both fields must contain something other than whitespace.
        if (username == null || username.trim().isEmpty()) {
            return AuthenticationResult.failure("Please enter a username.");
        }
        if (password == null || password.trim().isEmpty()) {
            return AuthenticationResult.failure("Please enter a password.");
        }

        // Rule 2: the username must exist in the repository.
        Optional<Administrator> administrator = administratorRepository.findByUsername(username);
        if (!administrator.isPresent()) {
            // The message is deliberately generic so that account details are not disclosed.
            return AuthenticationResult.failure("Invalid username or password. Please try again.");
        }

        // Rule 3: the password must match the stored password exactly.
        if (!administrator.get().getPassword().equals(password)) {
            return AuthenticationResult.failure("Invalid username or password. Please try again.");
        }

        // All rules passed, so the display name is prepared for the welcome page.
        return AuthenticationResult.success(displayNameFor(administrator.get().getUsername()));
    }

    /**
     * Converts the stored lower-case username into a presentable display name,
     * for example "xinle" becomes "Xinle".
     */
    private String displayNameFor(String username) {
        // Capitalise the first character and leave the rest unchanged.
        return username.substring(0, 1).toUpperCase(Locale.ROOT) + username.substring(1);
    }
}
