package ua.controller.admin;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.entity.Order;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.service.OrderService;
import ua.service.PlaceService;

import java.security.Principal;
import java.util.List;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminOrder")
@SessionAttributes("order")
public class AdminOrderController {

    private final OrderService service;

    private final PlaceService placeService;

    @Autowired
    public AdminOrderController(OrderService service, PlaceService placeService) {
        this.service = service;
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
    public String show(Model model, @PageableDefault Pageable pageable,
                       @ModelAttribute("orderFilter") OrderFilter filter) {
        model.addAttribute("meals", service.findAllMealsNames());
        model.addAttribute("places", service.findAllPlaceViews());
        model.addAttribute("orders", service.findAll(pageable, filter));
        model.addAttribute("statuses", service.findStatusForSearch());
        boolean hasContent = service.findAll(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminOrder";
        else
            return "redirect:/admin/adminOrder" + buildParams(hasContent, pageable, Strings.EMPTY);
    }

    @GetMapping("/updateStatus/{id}/{status}")
    public String update(@PathVariable Integer id, @PathVariable String status, Model model,
                         @PageableDefault Pageable pageable, @ModelAttribute("orderFilter") OrderFilter filter,
                         Principal principal) {
        Integer placeId = service.findOrderById(id).getPlace().getId();
        service.updateOrderStatus(id, status);
        List<Order> tableOrders = service.findOrderByPlaceId(placeId);
        boolean hasUnpaidOrders = false;
        for (Order order : tableOrders) {
            if (!order.getStatus().equals("Is paid")) {
                hasUnpaidOrders = true;
            }
        }
        if (!hasUnpaidOrders) {
            placeService.updatePlaceUserId(placeId, principal);
        }
        boolean hasContent = service.findAll(pageable, filter).hasContent();
        return "redirect:/admin/adminOrder" + buildParams(hasContent, pageable, Strings.EMPTY);
    }
}