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
import ua.model.filter.PlaceFilter;
import ua.model.request.PlaceRequest;
import ua.service.PlaceService;
import ua.validation.flag.PlaceFlag;

import java.security.Principal;
import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminPlace")
@SessionAttributes("place")
public class AdminPlaceController {

    private static final String REDIRECT_ADMIN_ADMIN_PLACE = "redirect:/admin/adminPlace";
    private final PlaceService service;

    private String error = "";

    @Autowired
    public AdminPlaceController(PlaceService service) {
        this.service = service;
    }

    @ModelAttribute("place")
    public PlaceRequest getForm() {
        return new PlaceRequest();
    }

    @ModelAttribute("placeFilter")
    public PlaceFilter getFilter() {
        return new PlaceFilter();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable, @ModelAttribute("placeFilter") PlaceFilter filter) {
        model.addAttribute("places", service.findAllView(pageable, filter));
        model.addAttribute("placesString", service.findAllPlacesCountOfPeople());
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminPlace";
        else
            return REDIRECT_ADMIN_ADMIN_PLACE + buildParams(hasContent, pageable, Strings.EMPTY);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, @PageableDefault Pageable pageable,
                         @ModelAttribute("placeFilter") PlaceFilter filter) {
        service.deletePlace(id);
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_PLACE + buildParams(hasContent, pageable, Strings.EMPTY);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't delete this place because it is used!";
        return REDIRECT_ADMIN_ADMIN_PLACE;
    }

    @PostMapping
    public String save(@ModelAttribute("place") @Validated(PlaceFlag.class) PlaceRequest request, BindingResult br,
                       Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("placeFilter") PlaceFilter filter) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        service.savePlace(request);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("placeFilter") PlaceFilter filter) {
        model.addAttribute("place", service.findOnePlaceRequest(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("placeFilter") PlaceFilter filter) {
        status.setComplete();
        boolean hasContent = service.findAllView(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_PLACE + buildParams(hasContent, pageable, Strings.EMPTY);
    }

    @GetMapping("/makeFree/{id}")
    public String makePlaceFree(@PathVariable Integer id) {
        service.makePlaceFree(id);
        return REDIRECT_ADMIN_ADMIN_PLACE;
    }
}