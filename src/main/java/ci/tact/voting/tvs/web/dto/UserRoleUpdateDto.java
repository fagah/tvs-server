package ci.tact.voting.tvs.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User role update request")
public class UserRoleUpdateDto {

        @NotEmpty(message = "At least one role must be specified")
    @Size(min = 1, message = "At least one role must be specified")
    @Schema(
        description = "Set of role names to assign to the user",
        example = "[\"USER\", \"ADMIN\"]"
    )
    private Set<String> roleNames;
}
