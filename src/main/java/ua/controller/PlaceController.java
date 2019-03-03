package ua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.service.PlaceService;

@Controller
public class PlaceController {

    private final PlaceService service;


    public PlaceController(PlaceService service) {
        this.service = service;
    }

    @GetMapping("/place")
    public String place(Model model) {
        model.addAttribute("places", service.findAllPlaceViews());
        model.addAttribute("myPlaces", service.findPlaceIdByUserId());
        return "place";
    }

    @GetMapping("/place/setUser/{id}")
    public String setUser(@PathVariable String id) {
        service.updatePlaceUserId(id);
        return "redirect:/place/{id}/order";
    }
}