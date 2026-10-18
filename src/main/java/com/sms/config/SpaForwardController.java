package com.sms.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Forwards Angular client-side routes to index.html when the browser
 * directly opens or refreshes a deep link such as /dashboard/students.
 *
 * API endpoints are deliberately not matched here.
 */
@Controller
public class SpaForwardController {

    @GetMapping({
        "/dashboard",
        "/dashboard/{*path}",
        "/login",
        "/forgot-password",
        "/verify-forgot-password-otp",
        "/reset-password",
        "/verify-account",
        "/verify-email"
    })
    public String forwardAngularRoutes() {
        return "forward:/index.html";
    }
}
