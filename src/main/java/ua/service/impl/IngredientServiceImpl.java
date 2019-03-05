package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.model.entity.Comment;
import ua.model.entity.Ingredient;
import ua.exception.CafeException;
import ua.model.filter.MealFilter;
import ua.model.filter.SimpleFilter;
import ua.dto.ComponentDTO;
import ua.dto.IngredientDTO;
import ua.dto.MealDTO;
import ua.repository.ComponentRepository;
import ua.repository.IngredientRepository;
import ua.repository.MealDTORepository;
import ua.service.IngredientService;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    private static final Logger LOG = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientRepository ingredientRepository;
    private final ComponentRepository componentRepository;
    private final MealDTORepository mealDTORepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, ComponentRepository componentRepository,
                                 MealDTORepository mealDTORepository) {
        this.ingredientRepository = ingredientRepository;
        this.componentRepository = componentRepository;
        this.mealDTORepository = mealDTORepository;
    }

    @Override
    public Page<Ingredient> findAll(Pageable pageable, SimpleFilter simpleFilter) {
        LOG.info("In 'findAll' method");
        return ingredientRepository.findAll(filterIngredients(simpleFilter), pageable);
    }

    private Specification<Ingredient> filterIngredients(SimpleFilter filter) {
        return (root, query, cb) -> {
            if (filter.getSearch().isEmpty()) return null;
            return cb.like(root.get("name"), filter.getSearch() + "%");
        };
    }

    @Override
    public IngredientDTO findIngredientDTO(String id) {
        return ingredientRepository.findIngredientDTO(id);
    }

    @Override
    public List<ComponentDTO> findComponentDTOByIngredientId(String id) {
        return componentRepository.findComponentDTOsByIngredientId(id);
    }

    @Override
    public Page<MealDTO> findAllMealDTOs(MealFilter filter, Pageable pageable) {
        return mealDTORepository.findAllMealDTOs(filter, pageable);
    }

    /**
     * Searching meals with ingredient
     */
    @Override
    public Page<MealDTO> searchMealsWithIngredient(String ingredientId, MealFilter mealFilter, Pageable pageable) {
        LOG.info("In 'searchMealsWithIngredient' method");
        List<ComponentDTO> componentsList = findComponentDTOByIngredientId(ingredientId);
        if (!componentsList.isEmpty()) {
            List<String> componentsIds = new ArrayList<>();
            for (ComponentDTO componentDTO : componentsList) {
                componentsIds.add(componentDTO.getId());
            }
            mealFilter.setComponentsId(componentsIds);
            return findAllMealDTOs(mealFilter, pageable);
        } else {
            return null;
        }
    }

    @Override
    public void updateCommentsList(String id, Comment newComment) {
        LOG.info("In 'updateCommentsList' method. Id = {}, NewComment = {}", id, newComment);
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Ingredient with id [%s} not found", id)));
        List<Comment> comments = ingredient.getComments();
        comments.add(newComment);
        ingredient.setComments(comments);
        ingredientRepository.save(ingredient);
        LOG.info("Exit from 'updateCommentsList' method");
    }

    @Override
    public List<Comment> findCommentList(String id) {
        return ingredientRepository.findCommentList(id);
    }

    @Override
    public void deleteById(String id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public Ingredient findById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Ingredient with id [%s} not found", id)));
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

}
