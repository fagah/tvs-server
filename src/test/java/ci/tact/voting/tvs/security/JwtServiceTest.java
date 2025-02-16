package ci.tact.voting.tvs.security;

import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", 
            "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000L);

        Role userRole = Role.builder()
                .name("USER")
                .build();

        user = User.builder()
                .id(1L)
                .username("0341234567")
                .fullName("Test User")
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertThat(token).isNotNull();
        assertThat(jwtService.extractUsername(token)).isEqualTo(user.getUsername());
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {
        // Act
        String refreshToken = jwtService.generateRefreshToken(user);

        // Assert
        assertThat(refreshToken).isNotNull();
        assertThat(jwtService.extractUsername(refreshToken)).isEqualTo(user.getUsername());
        assertThat(jwtService.isTokenValid(refreshToken, user)).isTrue();
    }

    @Test
    void isTokenValid_WhenTokenIsValid_ShouldReturnTrue() {
        // Arrange
        String token = jwtService.generateToken(user);

        // Act & Assert
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_WhenTokenIsExpired_ShouldReturnFalse() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L); // Expired
        String token = jwtService.generateToken(user);

        // Act & Assert
        assertThat(jwtService.isTokenValid(token, user)).isFalse();
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = jwtService.generateToken(user);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertThat(username).isEqualTo(user.getUsername());
    }
}