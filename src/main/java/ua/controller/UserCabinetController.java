package ua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.entity.User;
import ua.model.request.FileRequest;
import ua.service.UserService;

import java.io.IOException;

@Controller
public class UserCabinetController {

    private final UserService userService;

    public UserCabinetController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("fileRequest")
    public FileRequest getForm() {
        return new FileRequest();
    }

    /**
     * Show User cabinet page
     */
    @GetMapping("/userCabinet")
    public String userCabinet(Model model) {
        User user = userService.findCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("meals", userService.findUserMealViews());
        return "userCabinet";
    }

    /**
     * Attaching photo to User
     */
    @PostMapping("/userCabinet")
    public String saveFile(@ModelAttribute("fileRequest") FileRequest fileRequest) {
        try {
            userService.uploadPhotoToCloudinary(fileRequest.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/userCabinet";
    }
}
