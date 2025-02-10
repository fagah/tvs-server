package ci.tact.voting.tvs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.PermissionEntity;
import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.exception.ResourceNotFoundException;
import ci.tact.voting.tvs.repository.PermissionRepository;
import ci.tact.voting.tvs.repository.RoleRepository;
import ci.tact.voting.tvs.security.Permission;
import ci.tact.voting.tvs.web.dto.RoleDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    @Transactional
    public Role create(RoleDto request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + request.getName());
        }

        Set<PermissionEntity> permissions = getPermissionsFromCodes(request.getPermissionCodes());

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(permissions)
                .isDefault(request.isDefault())
                .build();

        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Long id, RoleDto request) {
        Role role = findById(id);
        
        if (!role.getName().equals(request.getName()) && 
            roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + request.getName());
        }

        Set<PermissionEntity> permissions = getPermissionsFromCodes(request.getPermissionCodes());

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(permissions);
        
        return roleRepository.save(role);
    }

    @Transactional
    public Role updatePermissions(Long id, Set<String> permissionCodes) {
        Role role = findById(id);
        Set<PermissionEntity> permissions = getPermissionsFromCodes(permissionCodes);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = findById(id);
        if (role.isDefault()) {
            throw new IllegalStateException("Cannot delete a default role");
        }
        roleRepository.delete(role);
    }

    @Transactional
    public void initializeDefaultRoles() {
        // Create ADMIN role if it doesn't exist
        if (!roleRepository.existsByName("ADMIN")) {
            Set<PermissionEntity> allPermissions = permissionRepository.findAll()
                    .stream()
                    .collect(Collectors.toSet());

            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrator role with full access")
                    .permissions(allPermissions)
                    .isDefault(true)
                    .build();
            roleRepository.save(adminRole);
        }

        // Create USER role if it doesn't exist
        if (!roleRepository.existsByName("USER")) {
            Set<String> userPermissions = Set.of(
                Permission.USER_READ.getPermission(),
                Permission.PARTY_READ.getPermission(),
                Permission.VOTE_READ.getPermission(),
                Permission.VOTE_CREATE.getPermission(),
                Permission.REPORT_READ.getPermission()
            );

            Set<PermissionEntity> permissions = getPermissionsFromCodes(userPermissions);

            Role userRole = Role.builder()
                    .name("USER")
                    .description("Standard user role")
                    .permissions(permissions)
                    .isDefault(true)
                    .build();
            roleRepository.save(userRole);
        }
    }

    private Set<PermissionEntity> getPermissionsFromCodes(Set<String> permissionCodes) {
        return permissionRepository.findByNameIn(permissionCodes.stream().collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toSet());
    }
}
