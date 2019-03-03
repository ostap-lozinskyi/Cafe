package ua.controller.admin;

import org.apache.logging.log4j.util.Strings;
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
import ua.model.filter.ComponentFilter;
import ua.model.request.ComponentRequest;
import ua.service.ComponentService;
import ua.validation.flag.ComponentFlag;

import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminComponent")
@SessionAttributes("component")
public class AdminComponentController {

    private static final String REDIRECT_ADMIN_ADMIN_COMPONENT = "redirect:/admin/adminComponent";
    private final ComponentService service;

    private String error = "";

    @Autowired
    public AdminComponentController(ComponentService service) {
        this.service = service;
    }

    @ModelAttribute("component")
    public ComponentRequest getForm() {
        return new ComponentRequest();
    }

    @ModelAttribute("componentFilter")
    public ComponentFilter getFilter() {
        return new ComponentFilter();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable,
                       @ModelAttribute("componentFilter") ComponentFilter filter) {
        model.addAttribute("ingredients", service.findAllIngredients());
        model.addAttribute("mss", service.findAllMss());
        model.addAttribute("components", service.findAllView(pageable, filter));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminComponent";
        else
            return REDIRECT_ADMIN_ADMIN_COMPONENT + buildParams(false, pageable, Strings.EMPTY);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("componentFilter") ComponentFilter filter) {
        service.deleteById(id);
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_COMPONENT + buildParams(hasContent, pageable, Strings.EMPTY);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't deleteById this component because it is used!";
        return REDIRECT_ADMIN_ADMIN_COMPONENT;
    }

    @PostMapping
    public String save(@ModelAttribute("component") @Validated(ComponentFlag.class) ComponentRequest request,
                       BindingResult br, Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("componentFilter") ComponentFilter filter) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        service.saveComponent(request);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("componentFilter") ComponentFilter filter) {
        model.addAttribute("component", service.findOneComponentRequest(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("componentFilter") ComponentFilter filter) {
        status.setComplete();
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_COMPONENT + buildParams(hasContent, pageable, Strings.EMPTY);
    }
}
