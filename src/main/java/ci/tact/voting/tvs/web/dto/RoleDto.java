package ci.tact.voting.tvs.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDto {
    @NotBlank(message = "Role name is required")
    private String name;
    
    private String description;
    
    @NotEmpty(message = "At least one permission is required")
    private Set<String> permissionCodes;
    
    private boolean isDefault;
}
