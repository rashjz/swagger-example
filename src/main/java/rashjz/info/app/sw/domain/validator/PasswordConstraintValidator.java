package rashjz.info.app.sw.domain.validator;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/*
(?=.*[0-9]) a digit must occur at least once
(?=.*[a-z]) a lower case letter must occur at least once
(?=.*[A-Z]) an upper case letter must occur at least once
(?=.*[@#$%^&+=]) a special character must occur at least once
(?=\\S+$) no whitespace allowed in the entire string
.{8,} at least 8 characters
*/
@Slf4j
@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String DIGIT_NUMBER_VALIDATION = ".*[0-9]";
    private static final String LOWER_CASE_VALIDATION = ".*[a-z]";
    private static final String UPPER_CASE_VALIDATION = ".*[A-Z]";
    private static final String SPECIAL_CHAR_VALIDATION = ".*[@#$%^&+=]";
    private static final String WHITESPACE_VALIDATION = "\\S+$";
    private static final String MIN_8_CHAR_VALIDATION = ".{8,}";


    @Override
    public void initialize(ValidPassword validPassword) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        log.info("is Valid invoked with password {}", password);
        List<String> errorMessages = new ArrayList<>();
        if (!password.matches(DIGIT_NUMBER_VALIDATION)) {
            errorMessages.add("at least 1 digit number");
        }
        if (!password.matches(LOWER_CASE_VALIDATION)) {
            errorMessages.add("at least 1 lower case");
        }
        if (!password.matches(UPPER_CASE_VALIDATION)) {
            errorMessages.add("at least 1 upper case");
        }
        if (!password.matches(SPECIAL_CHAR_VALIDATION)) {
            errorMessages.add("at least 1 special character");
        }
        if (!password.matches(WHITESPACE_VALIDATION)) {
            errorMessages.add("delete all whitespaces");
        }
        if (!password.matches(MIN_8_CHAR_VALIDATION)) {
            errorMessages.add("at last 8 characters");
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Please add " + Joiner.on(",").join(errorMessages))
                .addConstraintViolation();
        return false;
    }


}