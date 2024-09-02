package com.sparta.spartaeats.auditing;

import com.sparta.spartaeats.common.security.UserDetailsImpl;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Assuming the principal is an instance of your UserDetails implementation
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("userId : {}", userDetails.getUser().getId());
        return Optional.ofNullable(userDetails.getUser().getId());
    }
}
