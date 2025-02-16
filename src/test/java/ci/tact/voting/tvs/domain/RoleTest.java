package ci.tact.voting.tvs.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void builder_ShouldCreateRoleWithAllProperties() {
        // Arrange & Act
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator Role")
                .isDefault(true)
                .build();

        // Assert
        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo("ADMIN");
        assertThat(role.getDescription()).isEqualTo("Administrator Role");
        assertThat(role.isDefault()).isTrue();
    }

    @Test
    void equalsAndHashCode_ShouldBeBasedOnName() {
        // Arrange
        Role role1 = Role.builder().name("ADMIN").build();
        Role role2 = Role.builder().name("ADMIN").build();
        Role role3 = Role.builder().name("USER").build();

        // Assert
        assertThat(role1).isEqualTo(role2);
        assertThat(role1).isNotEqualTo(role3);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
        assertThat(role1.hashCode()).isNotEqualTo(role3.hashCode());
    }
}