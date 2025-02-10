package ci.tact.voting.tvs.web.dto;

import ci.tact.voting.tvs.web.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class LoginDto {

    @ValidPhoneNumber
    @Schema(description = "Phone number", example = "0341234567")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "password123")
    private String password;
}
