package ua.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import ua.repository.MealRepository;
import ua.validation.annotation.UniqueMeal;

@Component
public class MealValidator implements ConstraintValidator<UniqueMeal, String> {

	private final MealRepository mealRepository;

	public MealValidator(MealRepository mealRepository) {
		this.mealRepository = mealRepository;
	}

	@Override
	public void initialize(UniqueMeal constraintAnnotation) {
	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		return !mealRepository.existsByName(name);
	}

}
