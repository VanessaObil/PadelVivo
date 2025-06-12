package com.padel.api_padel.utils;


import com.padel.api_padel.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No autenticado");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails customUser) {
            return customUser.getUserId();
        }

        throw new RuntimeException("Tipo de usuario no soportado");
    }
}
