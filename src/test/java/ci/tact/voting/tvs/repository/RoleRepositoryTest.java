package ci.tact.voting.tvs.repository;

import ci.tact.voting.tvs.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        Role role = Role.builder()
                .name("ADMIN")
                .description("Administrator Role")
                .isDefault(true)
                .build();
        roleRepository.save(role);

        // Act
        Optional<Role> found = roleRepository.findByName("ADMIN");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("ADMIN");
    }

    @Test
    void findByName_WhenRoleDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Role> found = roleRepository.findByName("NON_EXISTENT");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void existsByName_WhenRoleExists_ShouldReturnTrue() {
        // Arrange
        Role role = Role.builder()
                .name("USER")
                .description("User Role")
                .isDefault(true)
                .build();
        roleRepository.save(role);

        // Act
        boolean exists = roleRepository.existsByName("USER");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_WhenRoleDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean exists = roleRepository.existsByName("NON_EXISTENT");

        // Assert
        assertThat(exists).isFalse();
    }
}