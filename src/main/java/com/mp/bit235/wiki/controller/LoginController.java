/*
 * BIT235 Assessment 2 - Part A: Wiki Administrator Login
 * Student: Xinle Xie - S1580929
 *
 * LoginController is the Controller part of MVC. It maps URLs to views,
 * binds the submitted form to the model, delegates the decision to the
 * service layer and chooses which page to render next.
 */
package com.mp.bit235.wiki.controller;

import com.mp.bit235.wiki.model.AuthenticationResult;
import com.mp.bit235.wiki.model.LoginForm;
import com.mp.bit235.wiki.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller // Declares a web controller whose methods return view names.
public class LoginController {

    private final AuthenticationService authenticationService;

    // The controller depends on the service, never on the repository directly.
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // The site root simply forwards the browser to the login page.
    @GetMapping("/")
    public String showRoot() {
        return "redirect:/login";
    }

    /**
     * Shows the login screen. A fresh LoginForm is added to the model so that
     * the Thymeleaf form has an object to bind to on the first request.
     */
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(name = "loggedOut", required = false) String loggedOut) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm()); // Empty form for a first visit.
        }
        if (loggedOut != null) {
            model.addAttribute("infoMessage", "You have been logged out."); // Feedback after logout.
        }
        return "login"; // Resolves to templates/login.html.
    }

    /**
     * Handles the form submission. Bean Validation runs first through @Valid;
     * if it fails, or the service rejects the credentials, the error page is
     * rendered with a message. Otherwise the browser is redirected to the
     * welcome page using the Post/Redirect/Get pattern.
     */
    @PostMapping("/login")
    public String submitLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // The first field error is enough for a single-message error page.
            String message = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Please check the details you entered.";
            model.addAttribute("errorMessage", message);
            return "error"; // Resolves to templates/error.html.
        }

        // The service applies the remaining business rules.
        AuthenticationResult result = authenticationService.authenticate(
                loginForm.getUsername(), loginForm.getPassword());

        if (!result.isSuccess()) {
            model.addAttribute("errorMessage", result.getMessage());
            return "error"; // Failed authentication is reported on the error page.
        }

        // Success: pass the display name as a query parameter and redirect.
        redirectAttributes.addAttribute("name", result.getDisplayName());
        return "redirect:/welcome";
    }

    /**
     * Renders the welcome page. A direct visit without a name is treated as
     * an unauthenticated request and sent back to the login screen.
     */
    @GetMapping("/welcome")
    public String showWelcome(@RequestParam(name = "name", required = false) String name,
                              Model model) {
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/login"; // No authenticated name was supplied.
        }
        model.addAttribute("displayName", name);
        return "welcome"; // Resolves to templates/welcome.html.
    }

    // Clears the session-less demo state by returning to the login page.
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("loggedOut", "true");
        return "redirect:/login";
    }
}
