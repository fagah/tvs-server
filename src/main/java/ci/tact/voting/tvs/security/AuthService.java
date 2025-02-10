package ci.tact.voting.tvs.security;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.domain.User;
import ci.tact.voting.tvs.exception.ResourceNotFoundException;
import ci.tact.voting.tvs.repository.RoleRepository;
import ci.tact.voting.tvs.repository.UserRepository;
import ci.tact.voting.tvs.web.dto.AuthDto;
import ci.tact.voting.tvs.web.dto.LoginDto;
import ci.tact.voting.tvs.web.dto.UserRegistrationDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDto register(UserRegistrationDto request, String ipAddress, List<String> rolesNames) {
        log.info("Registering new user with phone number: {}", request.getUsername());

        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        
        // Assign default role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "USER"));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        user = userRepository.save(user);
        log.info("User registered successfully with ID: {}", user.getId());

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthDto login(LoginDto request, String ipAddress) {
        log.info("Attempting login for phone number: {}", request.getUsername());

        // Authenticate user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        // Get user and generate tokens
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", request.getUsername()));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("User logged in successfully: {}", user.getId());
        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthDto refreshToken(String refreshToken, String ipAddress) {
        log.info("Attempting to refresh token");

        String phoneNumber = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));

        if (jwtService.isTokenValid(refreshToken, user)) {
            String accessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            
            log.info("Token refreshed successfully for user: {}", user.getId());
            return buildAuthResponse(user, accessToken, newRefreshToken);
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }

    @Transactional
    public void logout(String token, String ipAddress) {
        String phoneNumber = jwtService.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));

        // Here you might want to add the token to a blacklist or invalidate it
        log.info("User logged out successfully: {}", user.getId());
    }

    @Transactional(readOnly = true)
    public AuthDto validateToken(String token) {
        String phoneNumber = jwtService.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));

        if (jwtService.isTokenValid(token.substring(7), user)) {
            return buildAuthResponse(user, token.substring(7), null);
        }

        throw new IllegalArgumentException("Invalid token");
    }

    private AuthDto buildAuthResponse(User user, String accessToken, String refreshToken) {
        AuthDto response = new AuthDto();
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setPhoneNumber(user.getUsername());
        response.setFullName(user.getFullName());
        response.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return response;
    }
}