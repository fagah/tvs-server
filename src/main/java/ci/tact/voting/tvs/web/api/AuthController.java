package ci.tact.voting.tvs.web.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ci.tact.voting.tvs.security.AuthService;
import ci.tact.voting.tvs.web.dto.AuthDto;
import ci.tact.voting.tvs.web.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login with phone number and password")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDto.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(
            @Valid @RequestBody LoginDto request,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        AuthDto response = authService.login(request, httpRequest.getRemoteAddr());
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken())
            .body(response);
    }

    @Operation(summary = "Refresh authentication token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token successfully refreshed",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDto.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid or expired refresh token",
            content = @Content
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthDto> refreshToken(
            @RequestHeader("Authorization") String refreshToken,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        AuthDto response = authService.refreshToken(refreshToken, httpRequest.getRemoteAddr());
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken())
            .body(response);
    }

    @Operation(summary = "Logout user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully logged out"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid token",
            content = @Content
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String token,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        authService.logout(token, httpRequest.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token is valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDto.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid token",
            content = @Content
        )
    })
    @GetMapping("/validate")
    public ResponseEntity<AuthDto> validateToken(
            @RequestHeader("Authorization") String token) {
        AuthDto response = authService.validateToken(token);
        return ResponseEntity.ok(response);
    }
}
