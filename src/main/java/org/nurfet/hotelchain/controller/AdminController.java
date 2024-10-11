package org.nurfet.hotelchain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.dto.UserDto;
import org.nurfet.hotelchain.model.User;
import org.nurfet.hotelchain.model.Role;
import org.nurfet.hotelchain.service.RoleService;
import org.nurfet.hotelchain.service.UserService;
import org.nurfet.hotelchain.validation.ValidationGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    @GetMapping
    public String adminHome(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        String fullName = user.getFirstName() + " " + user.getLastName();

        Set<String> authorityRole = user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet());

        model.addAttribute("fullName", fullName);
        model.addAttribute("authorityRole", authorityRole);
        model.addAttribute("allUsers", userService.findAllUsers());
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("allRoles", roleService.findAllRoles());

        return "user/admin-home";
    }

    @GetMapping("/editUser/{id}")
    public String showEditForm(@PathVariable("id")Long id, Model model) {
        UserDto userDto = userService.prepareUserDtoForEdit(id);

        model.addAttribute("userDto", userDto);
        model.addAttribute("allRoles", roleService.findAllRoles());

        return "user/editUserByAdmin";
    }

    @PutMapping("/editUser")
    public String editUser(@Validated({ValidationGroup.Default.class, ValidationGroup.Specific.class}) @ModelAttribute("userDto")UserDto userDto,
                           BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "user/editUserByAdmin";
        }

        if (!userService.validateUserData(userDto)) {
            model.addAttribute("errorMessage", "Имя пользователя уже используется");
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "user/editUserByAdmin";
        }

        userService.updateUserFromDto(userDto);

        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findUserById(id);
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        userService.deleteUserById(id);


        redirectAttributes.addFlashAttribute("deleteMessage", "Пользователь " +
                firstName + " " + lastName + " удален/а");

        return "redirect:/admin";
    }

    @PostMapping
    public String registration(@Validated({ValidationGroup.Default.class, ValidationGroup.Specific.class}) @ModelAttribute("userDto")UserDto userDto,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allUsers", userService.findAllUsers());
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "user/admin-home";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("allUsers", userService.findAllUsers());
            model.addAttribute("allRoles", roleService.findAllRoles());
            model.addAttribute("errorMessage", "Имя пользователя уже используется");
            return "user/admin-home";
        }

        userService.createUserFromDto(userDto);
        redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно");

        return "redirect:/admin";
    }
}
