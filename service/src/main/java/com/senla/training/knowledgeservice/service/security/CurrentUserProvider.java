package com.senla.training.knowledgeservice.service.security;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public FullUserDetails extractFullUserDetailsFromCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            throw new BusinessException("Error at extracting current user:"
                    + " no authentication");
        }
        Object userDetails = authentication.getPrincipal();
        if (userDetails instanceof FullUserDetails) {
            return (FullUserDetails) userDetails;
        } else {
            throw new BusinessException("Error at extracting current user:"
                    + " unknown user type");
        }
    }
}
