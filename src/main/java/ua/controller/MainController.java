package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.model.entity.OrderStatus;
import ua.model.entity.User;
import ua.service.MealService;
import ua.service.OrderService;
import ua.service.UserService;

import java.util.Objects;

@Controller
public class MainController {

    private final MealService mealService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public MainController(MealService mealService, OrderService orderService, UserService userService) {
        this.mealService = mealService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        User user = userService.findCurrentUser();
        if (Objects.nonNull(user)) {
            model.addAttribute("message", "Hello " + user.getEmail());
            model.addAttribute("selectedMeals", orderService.findOrderDTOForUser(user.getId()));
        }
        model.addAttribute("meals", mealService.find5MealIndexDTOsByRate());
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/addMealToOrder/{mealId}")
    public String addMealToOrder(@PathVariable String mealId) {
        orderService.addMealToOrder(mealId);
        return "redirect:/";
    }

    @GetMapping("/setStatusAccepted")
    public String acceptOrder() {
        orderService.updateCurrentOrderStatus(OrderStatus.ACCEPTED);
        return "redirect:/";
    }

}
