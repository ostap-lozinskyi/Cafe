package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.model.filter.MealFilter;
import ua.dto.MealIndexDTO;
import ua.dto.MealDTO;

public interface MealDTORepository {

	Page<MealIndexDTO> findAllMealIndexDTOs(MealFilter filter, Pageable pageable);

	Page<MealDTO> findAllMealDTOs(MealFilter filter, Pageable pageable);
		
}
