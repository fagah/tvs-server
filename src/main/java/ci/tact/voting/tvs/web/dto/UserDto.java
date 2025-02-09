package ci.tact.voting.tvs.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response data")
public class UserDto {

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Phone number", example = "0341234567")
    private String phoneNumber;

    @Schema(description = "Email address", example = "user@tact.ci")
    private String email;

    @Schema(description = "Full name", example = "John Doe")
    private String fullName;

    @Schema(description = "List of role names")
    private Set<String> roles;

    @Schema(description = "Account status")
    private boolean enabled;

    @Schema(description = "Account creation date")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date")
    private LocalDateTime updatedAt;
}
