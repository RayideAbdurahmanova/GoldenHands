package com.matrix.Java._Spring.model.entity.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private static Claims getClaims(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null || !(authentication.getPrincipal() instanceof User)){
            throw new IllegalStateException("User is not authenticated");
        }
        return JwtClaimsHolder.getClaims();
    }

    public static Integer getCurrentUserId(){
        return getClaims().get("userId",Integer.class);
    }

    public static String getUsername(){
        return getClaims().get("username",String.class);
    }
}
