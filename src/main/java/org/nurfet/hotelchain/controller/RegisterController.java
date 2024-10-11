package org.nurfet.hotelchain.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.dto.UserDto;
import org.nurfet.hotelchain.exception.LoginException;
import org.nurfet.hotelchain.service.UserService;
import org.nurfet.hotelchain.validation.ValidationGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegisterController {

    private final UserService userService;

    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistrationForm(@Validated({ValidationGroup.Default.class}) @ModelAttribute("userDto") UserDto userDto,
                                          BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                          @SessionAttribute(required = false, name = "Authentication-Exception") LoginException loginException,
                                          HttpSession session, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("errorMessage", "Имя пользователя уже используется");
            return "registration";
        }

        userService.createUserFromDto(userDto);
        redirectAttributes.addFlashAttribute("registerUser", "Регистрация прошла успешно. " +
                "Пожалуйста войдите введя имя пользователя и пароль");

        if (loginException != null) {
            redirectAttributes.addFlashAttribute("authenticationException", loginException);
            session.removeAttribute("Authentication-Exception");
        } else {
            redirectAttributes.addFlashAttribute("authenticationException", null);
        }

        return "redirect:/login";
    }
}
