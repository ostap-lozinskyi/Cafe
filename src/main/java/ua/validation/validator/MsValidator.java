package ua.validation.validator;

import org.springframework.stereotype.Component;
import ua.repository.MsRepository;
import ua.validation.annotation.UniqueMs;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class MsValidator implements ConstraintValidator<UniqueMs, String> {

    private final MsRepository msRepository;

    public MsValidator(MsRepository msRepository) {
        this.msRepository = msRepository;
    }

    @Override
    public void initialize(UniqueMs constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !msRepository.existsByName(value);
    }

}
