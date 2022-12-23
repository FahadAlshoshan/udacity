package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    private String signUpView() {
        return "signup";
    }

    @PostMapping
    private String signUpUser(@Valid User user, Errors errors, Model model, RedirectAttributes re) {
        String errorMessages =
                errors.getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(","));
        if (errors.hasErrors()) {
            model.addAttribute("singUpError", true);
            model.addAttribute("ErrorMessage", errorMessages);
            return "signup";
        }
        int rows = userService.createUser(user);
        if (rows > 0) {
            re.addFlashAttribute("signup",true);
            return "redirect:/login";
        } else {
            logger.error("Error occurred while persisting user: " + user);
            model.addAttribute("signUpError", true);
            model.addAttribute("ErrorMessage", errorMessages);
            return "signup";
        }
    }
}
