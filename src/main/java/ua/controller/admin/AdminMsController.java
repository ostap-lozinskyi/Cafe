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
import ua.model.entity.Ms;
import ua.model.filter.SimpleFilter;
import ua.service.MsService;
import ua.validation.flag.MsFlag;

import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminMs")
@SessionAttributes("ms")
public class AdminMsController {

    private static final String REDIRECT_ADMIN_ADMIN_MS = "redirect:/admin/adminMs";
    private final MsService msService;

    private String error = "";

    @Autowired
    public AdminMsController(MsService msService) {
        this.msService = msService;
    }

    @ModelAttribute("ms")
    public Ms getForm() {
        return new Ms();
    }

    @ModelAttribute("filter")
    public SimpleFilter getFilter() {
        return new SimpleFilter();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("mss", msService.findAll(pageable, filter));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = msService.findAll(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminMs";
        else
            return REDIRECT_ADMIN_ADMIN_MS + buildParams(false, pageable, filter.getSearch());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        msService.deleteById(id);
        boolean hasContent = msService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_MS + buildParams(hasContent, pageable, filter.getSearch());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't deleteById this measuring unit because it is used!";
        return REDIRECT_ADMIN_ADMIN_MS;
    }

    @PostMapping
    public String save(@ModelAttribute("ms") @Validated(MsFlag.class) Ms ms, BindingResult br, Model model,
                       SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("filter") SimpleFilter filter) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        msService.save(ms);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("ms", msService.findById(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        status.setComplete();
        boolean hasContent = msService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_MS + buildParams(hasContent, pageable, filter.getSearch());
    }
}