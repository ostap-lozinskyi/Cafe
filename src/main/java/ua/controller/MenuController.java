package ua.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import ua.model.filter.MealFilter;
import ua.service.MealService;

@Controller
public class MenuController {

    private final MealService service;

    public MenuController(MealService service) {
        this.service = service;
    }

    @ModelAttribute("mealFilter")
    public MealFilter getFilter() {
        return new MealFilter();
    }

    @GetMapping("/menu")
    public String mealMenu(Model model, @ModelAttribute("mealFilter") MealFilter filter,
                           @PageableDefault Pageable pageable) {
        model.addAttribute("meals", service.findAllMealIndexDTOs(filter, pageable));
        model.addAttribute("cuisines", service.findAllCuisinesNames());
        return "menu";
    }

}