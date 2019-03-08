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
import ua.service.PlaceService;
import ua.validation.flag.OrderFlag;

import java.security.Principal;

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

    private final OrderService orderService;
    private final PlaceService placeService;

    @Autowired
    public OrderController(OrderService orderService, PlaceService placeService) {
        this.orderService = orderService;
        this.placeService = placeService;
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
    public String show(Model model, @ModelAttribute("orderFilter") OrderFilter filter) {
        model.addAttribute("meals", orderService.findAllMealsNames());
        model.addAttribute("selectedMeals", orderService.findOrderDTOForCurrentUser());
        model.addAttribute("places", placeService.findAllPlaceDTOs());
        return "order";
    }

    @GetMapping("/setStatusAccepted")
    public String acceptOrder() {
        orderService.updateCurrentOrderStatus(OrderStatus.ACCEPTED);
        return "redirect:/";
    }

    @GetMapping("/setPlace/{placeId}")
    public String setPlace(@PathVariable String placeId) {
        orderService.setPlace(placeId);
        return "redirect:/";
    }
}