package com.sms.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sms.security.entity.User;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    // =====================================================
    // Get Current Authentication
    // =====================================================

    public static Authentication getAuthentication() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    // =====================================================
    // Get Current User
    // =====================================================

    public static User getCurrentUser() {

        Authentication authentication =
                getAuthentication();


        if(authentication == null
                || !authentication.isAuthenticated()) {

            return null;
        }


        if(authentication.getPrincipal()
                instanceof User user) {

            return user;
        }


        return null;
    }

    // =====================================================
    // Get Current User Id
    // =====================================================

    public static Long getCurrentUserId() {

        User user =
                getCurrentUser();

        return user != null
                ? user.getId()
                : null;
    }

    // =====================================================
    // Get Current Username
    // =====================================================

    public static String getCurrentUsername() {

        User user =
                getCurrentUser();

        return user != null
                ? user.getUsername()
                : null;
    }

    // =====================================================
    // Get Current User Full Name
    // =====================================================

    public static String getCurrentFullName() {

        User user = getCurrentUser();

        if (user == null) {
            return null;
        }

        if (user.getStudent() != null) {
            return user.getStudent().getFullName();
        }

        return user.getUsername();   // fallback -> email
    }

    // =====================================================
    // Get Current Role
    // =====================================================

    public static String getCurrentRole() {

        User user =
                getCurrentUser();

        return user != null
                ? user.getRole().name()
                : null;
    }

}