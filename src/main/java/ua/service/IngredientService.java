package ua.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.entity.Comment;
import ua.entity.Ingredient;
import ua.model.filter.MealFilter;
import ua.model.filter.SimpleFilter;
import ua.model.view.ComponentView;
import ua.model.view.IngredientView;
import ua.model.view.MealView;

public interface IngredientService {

	Page<Ingredient> findAll(Pageable pageable, SimpleFilter filter);
	
	IngredientView findIngredientViewById(String id);
	
	List<ComponentView> findComponentViewByIngredientId(String id);
	
	Page<MealView> findAllMealView(MealFilter filter, Pageable pageable);
	
	Page<MealView> searchMealsWithIngredient(String ingredientId, MealFilter mealFilter, Pageable pageable);
	
	void updateCommentsList(String id, Comment comment);
	
	List<Comment> findCommentList(String id);

	void deleteById(String id);

	Ingredient findById(String id);

	void save(Ingredient ingredient);
}
