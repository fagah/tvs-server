package ci.tact.voting.tvs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.Role;
import ci.tact.voting.tvs.domain.User;
import ci.tact.voting.tvs.exception.ResourceNotFoundException;
import ci.tact.voting.tvs.repository.RoleRepository;
import ci.tact.voting.tvs.repository.UserRepository;
import ci.tact.voting.tvs.web.dto.PasswordChangeDto;
import ci.tact.voting.tvs.web.dto.UserDto;
import ci.tact.voting.tvs.web.dto.UserRoleUpdateDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserDto);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = getUserEntityById(id);

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getFullName() != null) {
            user.setFullName(userDto.getFullName());
        }

        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public void changePassword(Long id, PasswordChangeDto request) {
        User user = getUserEntityById(id);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserDto updateUserRoles(Long id, UserRoleUpdateDto request) {
        User user = getUserEntityById(id);
        
        Set<Role> roles = request.getRoleNames().stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto enableUser(Long id) {
        User user = getUserEntityById(id);
        user.setEnabled(true);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto disableUser(Long id) {
        User user = getUserEntityById(id);
        user.setEnabled(false);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private UserDto mapToUserDto(User user) {
        UserDto response = new UserDto();
        response.setId(user.getId());
        response.setPhoneNumber(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setEnabled(user.isEnabled());
        response.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return response;
    }
}
