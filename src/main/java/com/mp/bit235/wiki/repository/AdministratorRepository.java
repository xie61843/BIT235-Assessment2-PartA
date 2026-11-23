/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * AdministratorRepository is the persistence boundary of the application.
 * Part A does not use a database, so the single administrator record is
 * hard-coded here. In Part B this class is the only place that would need
 * to change when a real data store is introduced.
 */
package com.mp.bit235.wiki.repository;

import com.mp.bit235.wiki.model.Administrator;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;

@Repository // Marks this class as the repository layer bean managed by Spring.
public class AdministratorRepository {

    // The acceptable username is the student's first name in lower case.
    private static final String ADMIN_USERNAME = "xinle";
    // The acceptable password is fixed by the assignment brief.
    private static final String ADMIN_PASSWORD = "123";

    // The single hard-coded administrator record; no database lookup is performed.
    private static final Administrator ADMINISTRATOR =
            new Administrator(ADMIN_USERNAME, ADMIN_PASSWORD);

    /**
     * Finds the administrator whose username matches the supplied value.
     * The lookup is case-insensitive and ignores surrounding spaces so that
     * " Xinle " is treated as the same account as "xinle".
     *
     * @param username the raw username submitted from the login form
     * @return the matching administrator, or an empty Optional when not found
     */
    public Optional<Administrator> findByUsername(String username) {
        if (username == null) {
            return Optional.empty(); // A null value can never match a stored account.
        }
        // Normalising here keeps the comparison rule in one place.
        String normalised = username.trim().toLowerCase(Locale.ROOT);
        if (normalised.equals(ADMIN_USERNAME)) {
            return Optional.of(ADMINISTRATOR); // Username matched the stored record.
        }
        // No record matched, so the caller receives an empty result.
        return Optional.empty();
    }
}
