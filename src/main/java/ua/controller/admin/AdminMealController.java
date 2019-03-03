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
import ua.model.filter.MealFilter;
import ua.model.request.FileRequest;
import ua.model.request.MealRequest;
import ua.service.MealService;
import ua.validation.flag.MealFlag;

import java.io.IOException;
import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminMeal")
@SessionAttributes("meal")
public class AdminMealController {

    private static final String REDIRECT_ADMIN_ADMIN_MEAL = "redirect:/admin/adminMeal";
    private final MealService service;
    private String error = "";

    @Autowired
    public AdminMealController(MealService service) {
        this.service = service;
    }

    @ModelAttribute("meal")
    public MealRequest getForm() {
        return new MealRequest();
    }

    @ModelAttribute("mealFilter")
    public MealFilter getFilter() {
        return new MealFilter();
    }

    @ModelAttribute("fileRequest")
    public FileRequest getFile() {
        return new FileRequest();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("mealFilter") MealFilter filter) {
        model.addAttribute("cuisines", service.findAllCuisinesNames());
        model.addAttribute("components", service.findAllComponentsView());
        model.addAttribute("meals", service.findAllMealView(filter, pageable));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = service.findAllMealIndexView(filter, pageable).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminMeal";
        else
            return REDIRECT_ADMIN_ADMIN_MEAL + buildParams(false, pageable, filter.getSearch());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("mealFilter") MealFilter filter) {
        service.deleteMeal(id);
        boolean hasContent = service.findAllMealIndexView(filter, pageable).hasContent();
        return REDIRECT_ADMIN_ADMIN_MEAL + buildParams(hasContent, pageable, filter.getSearch());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't delete this meal because it is used!";
        return REDIRECT_ADMIN_ADMIN_MEAL;
    }

    @PostMapping
    public String save(@ModelAttribute("meal") @Validated(MealFlag.class) MealRequest request, BindingResult br,
                       Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("mealFilter") MealFilter filter, @ModelAttribute("fileRequest") FileRequest fileRequest) {
        if (br.hasErrors()) {
            return show(model, pageable, filter);
        }

        try {
            service.saveMeal(service.uploadPhotoToCloudinary(request, fileRequest.getFile()));
        } catch (IOException e) {
            service.saveMeal(request);
        }
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("mealFilter") MealFilter filter) {
        model.addAttribute("meal", service.findOneRequest(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("mealFilter") MealFilter filter) {
        status.setComplete();
        boolean hasContent = service.findAllMealIndexView(filter, pageable).hasContent();
        return REDIRECT_ADMIN_ADMIN_MEAL + buildParams(hasContent, pageable, filter.getSearch());
    }
}