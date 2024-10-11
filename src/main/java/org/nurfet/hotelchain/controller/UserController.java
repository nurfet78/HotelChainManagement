package org.nurfet.hotelchain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.hotelchain.dto.UserDto;
import org.nurfet.hotelchain.model.User;
import org.nurfet.hotelchain.service.UserService;
import org.nurfet.hotelchain.validation.ValidationGroup;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        String fullName = user.getFirstName() + " " + user.getLastName();

        model.addAttribute("user", fullName);

        return "user/user-home";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.findByUsername(userDetails.getUsername());

            model.addAttribute("user", user);
        }

        return "user/profile-page";
    }

    @GetMapping("/editUser")
    public String showEditForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        UserDto userDto = userService.prepareUserDtoForEdit(user.getId());
        model.addAttribute("userDto", userDto);

        return "user/editUser";
    }

    @PutMapping("/editUser")
    public String editUser(@Validated(ValidationGroup.Default.class) @ModelAttribute("userDto") UserDto userDto,
                           BindingResult bindingResult, Model model, Principal principal) {


        if (bindingResult.hasErrors()) {
            return "user/editUser";
        }

        User currentUser = userService.findByUsername(principal.getName());

        // При редактировании своего профиля пользователем, роли оставляем прежними
        Set<String> currentRoles = currentUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        if (currentRoles.contains("ROLE_ADMIN")) {
            if (currentRoles.contains("ROLE_USER")) {
                userDto.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
            } else {
                userDto.setRoles(Set.of("ROLE_ADMIN"));
            }
        } else {
            userDto.setRoles(Set.of("ROLE_USER"));
        }

        if (!userService.validateUserData(userDto)) {
            model.addAttribute("errorMessage", "Имя пользователя уже используется");
            return "user/editUser";
        }


        userService.updateUserFromDto(userDto);


        // обновление контекста безопасности текущей сессии пользователя.
        User updatedUser = userService.findByUsername(principal.getName());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/user/profile";
    }
}
