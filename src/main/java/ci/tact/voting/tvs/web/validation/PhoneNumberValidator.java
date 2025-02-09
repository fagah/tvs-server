package ci.tact.voting.tvs.web.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private String regionCode;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        regionCode = constraintAnnotation.region();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        try {
            // Parse the number
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, regionCode);

            // Check if it's a valid number for the region
            boolean isValid = phoneUtil.isValidNumberForRegion(number, regionCode);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid phone number for region " + regionCode)
                      .addConstraintViolation();
            }

            return isValid;

        } catch (NumberParseException e) {
            log.debug("Error parsing phone number: {}", e.getMessage());
            
            // Customize error message based on the type of error
            context.disableDefaultConstraintViolation();
            String errorMessage = switch (e.getErrorType()) {
                case INVALID_COUNTRY_CODE -> "Invalid country code";
                case TOO_SHORT_NSN -> "Number is too short";
                case TOO_LONG -> "Number is too long";
                default -> "Invalid phone number format";
            };
            
            context.buildConstraintViolationWithTemplate(errorMessage)
                  .addConstraintViolation();
                  
            return false;
        }
    }
}