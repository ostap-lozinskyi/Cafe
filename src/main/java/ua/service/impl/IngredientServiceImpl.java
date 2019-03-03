package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.entity.Comment;
import ua.entity.Ingredient;
import ua.exception.CafeException;
import ua.model.filter.MealFilter;
import ua.model.filter.SimpleFilter;
import ua.model.view.ComponentView;
import ua.model.view.IngredientView;
import ua.model.view.MealView;
import ua.repository.ComponentRepository;
import ua.repository.IngredientRepository;
import ua.repository.MealViewRepository;
import ua.service.IngredientService;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientServiceImpl extends CrudServiceImpl<Ingredient, String> implements IngredientService {
    private static final Logger LOG = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientRepository ingredientRepository;
    private final ComponentRepository componentRepository;
    private final MealViewRepository mealViewRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, ComponentRepository componentRepository,
                                 MealViewRepository mealViewRepository) {
        super(ingredientRepository);
        this.ingredientRepository = ingredientRepository;
        this.componentRepository = componentRepository;
        this.mealViewRepository = mealViewRepository;
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
    public IngredientView findIngredientViewById(String id) {
        return ingredientRepository.findIngredientViewById(id);
    }

    @Override
    public List<ComponentView> findComponentViewByIngredientId(String id) {
        return componentRepository.findComponentViewByIngredientId(id);
    }

    @Override
    public Page<MealView> findAllMealView(MealFilter filter, Pageable pageable) {
        return mealViewRepository.findAllMealView(filter, pageable);
    }

    /**
     * Searching meals with ingredient
     */
    @Override
    public Page<MealView> searchMealsWithIngredient(String ingredientId, MealFilter mealFilter, Pageable pageable) {
        LOG.info("In 'searchMealsWithIngredient' method");
        List<ComponentView> componentsList = findComponentViewByIngredientId(ingredientId);
        if (!componentsList.isEmpty()) {
            List<String> componentsIds = new ArrayList<>();
            for (ComponentView componentView : componentsList) {
                componentsIds.add(componentView.getId());
            }
            mealFilter.setComponentsId(componentsIds);
            return findAllMealView(mealFilter, pageable);
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

}
