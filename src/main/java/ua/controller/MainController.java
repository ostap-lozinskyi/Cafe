package ua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.exception.CafeException;
import ua.model.entity.Meal;
import ua.model.entity.Place;
import ua.model.entity.User;
import ua.model.request.OrderRequest;
import ua.service.MealService;
import ua.service.OrderService;
import ua.service.PlaceService;
import ua.service.UserService;

import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    private final MealService mealService;
    private final OrderService orderService;
    private final UserService userService;
    private final PlaceService placeService;

    @Autowired
    public MainController(MealService mealService, OrderService orderService, UserService userService,
                          PlaceService placeService) {
        this.mealService = mealService;
        this.orderService = orderService;
        this.userService = userService;
        this.placeService = placeService;
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
        String userId = userService.findCurrentUser().getId();
        OrderRequest orderRequest = orderService.findOrderRequestByUserId(userId);

        Meal newOrderedMeal = mealService.findMealById(mealId);
        List<Meal> alreadyOrderedMeals = orderRequest.getMeals();
        alreadyOrderedMeals.add(newOrderedMeal);
        orderRequest.setMeals(alreadyOrderedMeals);

        orderRequest.setUserId(userId);
        orderRequest.setStatus("Ordered");

        Place place = placeService.findPlaceByName("Remote");
        if (Objects.isNull(place)) {
            throw new CafeException("Place with name 'Remote' not found");
        }
        orderRequest.setPlace(place);
        orderService.saveOrder(orderRequest);
        return "redirect:/";
    }

}
