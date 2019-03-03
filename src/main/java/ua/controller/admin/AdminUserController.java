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
import ua.entity.Role;
import ua.model.filter.UserFilter;
import ua.model.request.FileRequest;
import ua.model.request.RegistrationRequest;
import ua.service.UserService;
import ua.validation.flag.UserFlag;

import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminUser")
@SessionAttributes("meal")
public class AdminUserController {

    private static final String REDIRECT_ADMIN_ADMIN_USER = "redirect:/admin/adminUser";
    private final UserService userService;
    private String error = "";

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public RegistrationRequest getForm() {
        return new RegistrationRequest();
    }

    @ModelAttribute("userFilter")
    public UserFilter getFilter() {
        return new UserFilter();
    }

    @ModelAttribute("fileRequest")
    public FileRequest getFile() {
        return new FileRequest();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("userFilter") UserFilter filter) {
        model.addAttribute("users", userService.findAllUsers(pageable, filter));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = userService.findAllUsers(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminUser";
        else
            return REDIRECT_ADMIN_ADMIN_USER + buildParams(hasContent, pageable, filter.getSearch());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("userFilter") UserFilter filter) {
        userService.deleteById(id);
        boolean hasContent = userService.findAllUsers(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_USER + buildParams(hasContent, pageable, filter.getSearch());
    }

    @GetMapping("/setDefaultPhoto/{id}")
    public String setDefaultPhoto(@PathVariable String id, @PageableDefault Pageable pageable,
                                  @ModelAttribute("userFilter") UserFilter filter) {
        userService.setDefaultPhoto(id);
        boolean hasContent = userService.findAllUsers(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_USER + buildParams(hasContent, pageable, filter.getSearch());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't deleteById this user because it is used!";
        return REDIRECT_ADMIN_ADMIN_USER;
    }

    @PostMapping
    public String save(@ModelAttribute("user") @Validated(UserFlag.class) RegistrationRequest request, BindingResult br,
                       Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("userFilter") UserFilter filter, @ModelAttribute("fileRequest") FileRequest fileRequest) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        request.setRole(Role.ROLE_ADMIN);
        userService.saveUser(request);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/updateRole/{id}/{role}")
    public String updateRole(@PathVariable String id, @PathVariable Role role, Model model, @PageableDefault Pageable pageable,
                             @ModelAttribute("userFilter") UserFilter filter) {
        userService.updateRole(id, role);
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("userFilter") UserFilter filter) {
        status.setComplete();
        boolean hasContent = userService.findAllUsers(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_USER + buildParams(hasContent, pageable, filter.getSearch());
    }
}