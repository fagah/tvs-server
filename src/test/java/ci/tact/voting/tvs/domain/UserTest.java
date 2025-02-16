package ci.tact.voting.tvs.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("USER")
                .build();

        user = User.builder()
                .id(1L)
                .username("0341234567")
                .password("password")
                .email("test@example.com")
                .fullName("Test User")
                .roles(new HashSet<>())
                .enabled(true)
                .build();
    }

    @Test
    void builder_ShouldCreateUserWithAllProperties() {
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("0341234567");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFullName()).isEqualTo("Test User");
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void addRole_ShouldAddRoleToUser() {
        // Act
        user.addRole(role);

        // Assert
        assertThat(user.getRoles()).contains(role);
    }

    @Test
    void removeRole_ShouldRemoveRoleFromUser() {
        // Arrange
        user.addRole(role);

        // Act
        user.removeRole(role);

        // Assert
        assertThat(user.getRoles()).doesNotContain(role);
    }

    @Test
    void userDetails_ShouldImplementCorrectly() {
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void getAuthorities_ShouldReturnCorrectAuthorities() {
        // Arrange
        user.addRole(role);

        // Act & Assert
        assertThat(user.getAuthorities())
                .extracting("authority")
                .contains("ROLE_USER");
    }
}