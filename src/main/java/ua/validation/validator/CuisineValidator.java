package ua.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import ua.repository.CuisineRepository;
import ua.validation.annotation.UniqueCuisine;

@Component
public class CuisineValidator implements ConstraintValidator<UniqueCuisine, String> {

	private final CuisineRepository cuisineRepository;

	public CuisineValidator(CuisineRepository cuisineRepository) {
		this.cuisineRepository = cuisineRepository;
	}

	@Override
	public void initialize(UniqueCuisine constraintAnnotation) {
	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		return !cuisineRepository.existsByName(name);
	}

}
