package ci.tact.voting.tvs.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ci.tact.voting.tvs.domain.User;

@Component("securityExpressions")
public class SecurityExpressions {

    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        return user.getAllPermissions().stream()
                .anyMatch(p -> p.getPermission().equals(permission));
    }

    // Method accepting String varargs
    public boolean hasAnyPermission(String... permissions) {
        return hasAnyPermissionInternal(Arrays.asList(permissions));
    }

    // Method accepting List<String>
    public boolean hasAnyPermission(List<String> permissions) {
        return hasAnyPermissionInternal(permissions);
    }

    // Method accepting Set<String>
    public boolean hasAnyPermission(Set<String> permissions) {
        return hasAnyPermissionInternal(permissions);
    }

    // Internal method to handle the permission check
    private boolean hasAnyPermissionInternal(Collection<String> permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        Set<Permission> userPermissions = user.getAllPermissions();

        return permissions.stream()
                .anyMatch(requiredPerm -> 
                    userPermissions.stream()
                        .anyMatch(userPerm -> userPerm.getPermission().equals(requiredPerm))
                );
    }
}