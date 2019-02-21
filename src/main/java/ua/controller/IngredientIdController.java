package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.entity.Comment;
import ua.entity.Role;
import ua.model.filter.MealFilter;
import ua.model.request.CommentRequest;
import ua.model.view.IngredientView;
import ua.model.view.MealView;
import ua.service.CommentService;
import ua.service.IngredientService;
import ua.service.UserService;
import ua.validation.flag.CommentFlag;

import java.security.Principal;

import static ua.controller.ControllerUtils.buildParams;

@Controller
public class IngredientIdController {

    private static final String REDIRECT_INGREDIENT_ID = "redirect:/ingredient/{id}";
    private final IngredientService ingredientService;

    private final CommentService commentService;

    private final UserService userService;

    private Page<MealView> mealViews;

    private String error = "";

    @Autowired
    public IngredientIdController(IngredientService service, CommentService commentService, UserService userService) {
        this.ingredientService = service;
        this.commentService = commentService;
        this.userService = userService;
    }

    @ModelAttribute("mealFilter")
    public MealFilter getFilter() {
        return new MealFilter();
    }

    @ModelAttribute("comment")
    public CommentRequest getForm() {
        return new CommentRequest();
    }

    /**
     * Show Ingredient page
     */
    @GetMapping("/ingredient/{id}")
    public String show(Model model, @PathVariable Integer id, @PageableDefault Pageable pageable,
                       @ModelAttribute("mealFilter") MealFilter filter) {
        IngredientView ingredient = ingredientService.findIngredientViewById(id);
        ingredient.setComments(ingredientService.findCommentList(id));
        model.addAttribute("ingredient", ingredient);
        mealViews = ingredientService.searchMealsWithIngredient(id, filter, pageable);
        model.addAttribute("meals", mealViews);
        model.addAttribute("tasteMeal", error);
        error = "";
        boolean hasContent = ingredientService.findAllMealView(filter, pageable).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "ingredientId";
        else
            return REDIRECT_INGREDIENT_ID + buildParams(false, pageable, filter.getSearch());
    }

    /**
     * Verifying comment possibility
     */
    @PostMapping("/ingredient/{id}")
    public String ingredientIdComment(@PathVariable Integer id, @ModelAttribute("comment") @Validated(CommentFlag.class)
            CommentRequest commentRequest, Principal principal) {
        if (userService.findUserByEmail(principal.getName()).getRole() == Role.ROLE_ADMIN) {
            saveComment(id, commentRequest, principal);
            return REDIRECT_INGREDIENT_ID;
        }
        if (mealViews != null) {
            if (userService.findMealInUserOrders(mealViews, principal)) {
                saveComment(id, commentRequest, principal);
            } else {
                error = "Taste the ingredient before the evaluation";
            }
        } else {
            error = "This ingredient is not used";
        }
        return REDIRECT_INGREDIENT_ID;
    }

    /**
     * Saving comment
     */
    private void saveComment(Integer id, CommentRequest commentRequest, Principal principal) {
        Integer commentId = commentService.save(commentRequest, principal);
        Comment comment = commentService.findById(commentId);
        ingredientService.updateCommentsList(id, comment);
    }
}