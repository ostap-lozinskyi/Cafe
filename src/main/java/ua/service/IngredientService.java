package ua.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.dto.MealDTO;
import ua.model.entity.Comment;
import ua.model.entity.Ingredient;
import ua.model.filter.MealFilter;
import ua.model.filter.SimpleFilter;
import ua.dto.ComponentDTO;
import ua.dto.IngredientDTO;

public interface IngredientService {

	Page<Ingredient> findAll(Pageable pageable, SimpleFilter filter);
	
	IngredientDTO findIngredientDTO(String id);
	
	List<ComponentDTO> findComponentDTOByIngredientId(String id);
	
	Page<MealDTO> findAllMealDTOs(MealFilter filter, Pageable pageable);
	
	Page<MealDTO> searchMealsWithIngredient(String ingredientId, MealFilter mealFilter, Pageable pageable);
	
	void updateCommentsList(String id, Comment comment);
	
	List<Comment> findCommentList(String id);

	void deleteById(String id);

	Ingredient findById(String id);

	void save(Ingredient ingredient);
}
