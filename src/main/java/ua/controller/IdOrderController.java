package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.model.entity.OrderStatus;
import ua.model.entity.Place;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.service.OrderService;
import ua.validation.flag.OrderFlag;

import java.security.Principal;

@Controller
@RequestMapping("/place/{id}/order")
@SessionAttributes("order2")
public class IdOrderController {

    private final OrderService service;

    @Autowired
    public IdOrderController(OrderService service) {
        this.service = service;
    }

    @ModelAttribute("order")
    public OrderRequest getForm() {
        return new OrderRequest();
    }

    @ModelAttribute("orderFilter")
    public OrderFilter getFilter() {
        return new OrderFilter();
    }

    @GetMapping
    public String show(@PathVariable String id, Model model, @ModelAttribute("orderFilter") OrderFilter filter) {
        model.addAttribute("meals", service.findAllMealsNames());
        model.addAttribute("orders", service.findOrderDTOsForTable(id));

        model.addAttribute("placeCurrent", service.findPlaceDTO(id));
        return "idOrder";
    }

    @PostMapping
    public String save(@PathVariable String id, @ModelAttribute("order") @Validated(OrderFlag.class) OrderRequest request,
                       @ModelAttribute("orderFilter") OrderFilter filter, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Place place = new Place();
        place.setId(id);
        request.setPlace(place);
        request.setStatus(OrderStatus.ACCEPTED);
        if (!request.getMeals().isEmpty())
            service.saveOrder(request);
        return "redirect:/place/{id}/order";
    }
}