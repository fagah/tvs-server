package ci.tact.voting.tvs.web.dto;

import ci.tact.voting.tvs.web.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDto {

    @ValidPhoneNumber(message = "Please provide a valid phone number")
    private String username;

    @NotEmpty(message = "Full name is required and cannot be empty")
    private String fullName;

    @NotEmpty(message = "Email is required and cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "Password is required and cannot be empty")
    private String password;

    @NotEmpty(message = "Confirm password is required and cannot be empty")
    private String confirmPassword;
}
