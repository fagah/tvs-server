package ci.tact.voting.tvs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.exception.ResourceNotFoundException;
import ci.tact.voting.tvs.repository.RoleRepository;
import ci.tact.voting.tvs.security.Permission;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

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
    public Role create(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + role.getName());
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Long id, Role roleDetails) {
        Role role = findById(id);
        
        if (!role.getName().equals(roleDetails.getName()) && 
            roleRepository.existsByName(roleDetails.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + roleDetails.getName());
        }

        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        role.setPermissions(roleDetails.getPermissions());
        
        return roleRepository.save(role);
    }

    @Transactional
    public Role updatePermissions(Long id, Set<Permission> permissions) {
        Role role = findById(id);
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
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrator role with full access")
                    .permissions(Set.of(Permission.values())) // All permissions
                    .isDefault(true)
                    .build();
            roleRepository.save(adminRole);
        }

        // Create USER role if it doesn't exist
        if (!roleRepository.existsByName("USER")) {
            Role userRole = Role.builder()
                    .name("USER")
                    .description("Standard user role")
                    .permissions(Set.of(
                        Permission.USER_READ,
                        Permission.PARTY_READ,
                        Permission.VOTE_READ,
                        Permission.VOTE_CREATE,
                        Permission.REPORT_READ
                    ))
                    .isDefault(true)
                    .build();
            roleRepository.save(userRole);
        }
    }
}
