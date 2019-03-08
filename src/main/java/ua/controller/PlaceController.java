package ua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.service.PlaceService;

@Controller
public class PlaceController {

    private final PlaceService placeService;


    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/place")
    public String place(Model model) {
        model.addAttribute("places", placeService.findAllPlaceDTOs());
        model.addAttribute("myPlaces", placeService.findPlaceIdByUserId());
        return "place";
    }

    @GetMapping("/place/setUser/{id}")
    public String setUser(@PathVariable String id) {
        placeService.updatePlaceUserId(id);
        return "redirect:/place/{id}/order";
    }
}