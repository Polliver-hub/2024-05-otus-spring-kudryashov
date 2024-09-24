package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.User;
import ru.otus.hw.services.RegistrationService;
import ru.otus.hw.utils.UserValidator;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    private final UserValidator userValidator;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        registrationService.register(user);
        return "redirect:/login";
    }
}
