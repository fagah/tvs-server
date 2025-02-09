package ci.tact.voting.tvs.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ci.tact.voting.tvs.service.UserService;
import ci.tact.voting.tvs.web.dto.PasswordChangeDto;
import ci.tact.voting.tvs.web.dto.UserDto;
import ci.tact.voting.tvs.web.dto.UserRoleUpdateDto;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "Get all users (paginated)")
    public ResponseEntity<Page<UserDto>> getPaginatedUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "Get user by USERNAME")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("@securityExpressions.hasPermission('user:update') or @securityExpressions.isSelfOrAdmin(#id)")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeDto request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "Update user roles")
    public ResponseEntity<UserDto> updateUserRoles(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateDto userRoleUpdateDto) {
        return ResponseEntity.ok(userService.updateUserRoles(id, userRoleUpdateDto));
    }

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "Enable user")
    public ResponseEntity<UserDto> enableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.enableUser(id));
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "Disable user")
    public ResponseEntity<UserDto> disableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.disableUser(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}