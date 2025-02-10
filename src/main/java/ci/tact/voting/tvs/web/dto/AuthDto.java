package ci.tact.voting.tvs.web.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication response")
public class AuthDto {

    @Schema(description = "JWT access token")
    private String token;
    
    @Schema(description = "JWT refresh token")
    private String refreshToken;
    
    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";
    
    @Schema(description = "Token expiration time in seconds")
    private Long expiresIn;
    
    @Schema(description = "User's phone number")
    private String phoneNumber;
    
    @Schema(description = "User's full name")
    private String fullName;
    
    @Schema(description = "User's roles")
    private Set<String> roles;
}
