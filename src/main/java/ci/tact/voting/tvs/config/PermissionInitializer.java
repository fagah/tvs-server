package ci.tact.voting.tvs.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.PermissionEntity;
import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.repository.PermissionRepository;
import ci.tact.voting.tvs.repository.RoleRepository;
import ci.tact.voting.tvs.security.Permission;
import ci.tact.voting.tvs.service.RoleService;
import ci.tact.voting.tvs.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInitializer {

    private final PermissionRepository permissionRepository;
    private final RoleService roleService;
    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializePermissions() {
        log.info("Initializing permissions...");
        // load if not exists
        if (permissionRepository.count() > 0) {
            log.info("Permissions already initialized");
            return;
        }

        List<PermissionEntity> permissions = Arrays.stream(Permission.values())
                .map(permission -> new PermissionEntity(
                    permission.getPermission(),
                    permission.getDescription(),
                    permission.getCategory()
                ))
                .collect(Collectors.toList());

        permissionRepository.saveAll(permissions);
        
        log.info("Initialized {} permissions", permissions.size());

        // Initialize roles
        if(roleService.findAll().isEmpty()) {
            roleService.initializeDefaultRoles();
        }
        
    }

    private void initUsers() {
        // initialize users with an admin and a user
        userService.initializeUsers();

    }
}
