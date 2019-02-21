package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.model.filter.SimpleFilter;
import ua.service.IngredientService;

import static ua.controller.ControllerUtils.buildParams;

@Controller
public class IngredientController {

    private final IngredientService service;

    @Autowired
    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @ModelAttribute("filter")
    public SimpleFilter getFilter() {
        return new SimpleFilter();
    }

    @GetMapping("/ingredient")
    public String ingredient(Model model, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("ingredients", service.findAll(pageable, filter));
        boolean hasContent = service.findAll(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "ingredient";
        else
            return "redirect:/ingredient" + buildParams(false, pageable, filter.getSearch());
    }
}