package ua.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ua.model.entity.Ingredient;
import ua.model.filter.SimpleFilter;
import ua.service.IngredientService;
import ua.validation.flag.IngredientFlag;

import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminIngredient")
@SessionAttributes("ingredient")
public class AdminIngredientController {

    private static final String REDIRECT_ADMIN_ADMIN_INGREDIENT = "redirect:/admin/adminIngredient";
    private final IngredientService ingredientService;

    private String error = "";

    @Autowired
    public AdminIngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @ModelAttribute("ingredient")
    public Ingredient getForm() {
        return new Ingredient();
    }

    @ModelAttribute("filter")
    public SimpleFilter getFilter() {
        return new SimpleFilter();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("ingredients", ingredientService.findAll(pageable, filter));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = ingredientService.findAll(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminIngredient";
        else
            return REDIRECT_ADMIN_ADMIN_INGREDIENT + buildParams(false, pageable, filter.getSearch());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        ingredientService.deleteById(id);
        boolean hasContent = ingredientService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_INGREDIENT + buildParams(hasContent, pageable, filter.getSearch());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't deleteById this cuisine because it is used!";
        return REDIRECT_ADMIN_ADMIN_INGREDIENT;
    }

    @PostMapping
    public String save(@ModelAttribute("ingredient") @Validated(IngredientFlag.class) Ingredient ingredient,
                       BindingResult br, Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("filter") SimpleFilter filter) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        ingredientService.save(ingredient);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("ingredient", ingredientService.findById(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        status.setComplete();
        boolean hasContent = ingredientService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_INGREDIENT + buildParams(hasContent, pageable, filter.getSearch());
    }
}