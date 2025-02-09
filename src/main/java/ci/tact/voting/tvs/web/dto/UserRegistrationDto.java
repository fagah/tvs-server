package ci.tact.voting.tvs.web.dto;

import ci.tact.voting.tvs.web.validation.ValidPhoneNumber;

public class UserRegistrationDto {

    @ValidPhoneNumber(message = "Please provide a valid phone number")
    private String username;
}
