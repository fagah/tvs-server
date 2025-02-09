package ci.tact.voting.tvs.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PhoneNumberUtils {
    
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    
    @Value("${tact.country.code}")    
    private String COUNTRY_CODE;

    /**
     * Format phone number to E164 format
     */
    public String formatToE164(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, COUNTRY_CODE);
            if (phoneUtil.isValidNumberForRegion(number, COUNTRY_CODE)) {
                return phoneUtil.format(number, com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.E164);
            }
        } catch (NumberParseException e) {
            log.error("Error formatting phone number: {}", e.getMessage());
        }
        throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
    }

    /**
     * Format phone number to national format
     */
    public String formatToNational(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, COUNTRY_CODE);
            if (phoneUtil.isValidNumberForRegion(number, COUNTRY_CODE)) {
                return phoneUtil.format(number, com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            }
        } catch (NumberParseException e) {
            log.error("Error formatting phone number: {}", e.getMessage());
        }
        throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
    }

    /**
     * Check if phone number is valid
     */
    public boolean isValid(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, COUNTRY_CODE);
            return phoneUtil.isValidNumberForRegion(number, COUNTRY_CODE);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     * Get carrier for phone number
     */
    public String getOperator(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, COUNTRY_CODE);
            if (phoneUtil.isValidNumberForRegion(number, COUNTRY_CODE)) {
                String nationalNumber = String.valueOf(number.getNationalNumber());
                String prefix = nationalNumber.substring(0, 2);
                return switch (prefix) {
                    case "32" -> "Orange";
                    case "33" -> "Airtel";
                    case "34" -> "Telma";
                    case "38" -> "Telma";
                    default -> "Unknown";
                };
            }
        } catch (NumberParseException e) {
            log.error("Error getting operator: {}", e.getMessage());
        }
        throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
    }
}
