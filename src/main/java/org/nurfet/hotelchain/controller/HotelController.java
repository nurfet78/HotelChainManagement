package org.nurfet.hotelchain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;


    @GetMapping
    public String getAllHotels(Model model) {
        List<Hotel> hotels = hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);
        return "hotels/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "hotels/new";
    }

    @PostMapping
    public String createHotel(@Valid @ModelAttribute("hotel") Hotel hotel, BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "hotels/new";
        }

        hotelService.createHotel(hotel);
        redirectAttributes.addFlashAttribute("message", "Отель успешно добавлен!");
        return "redirect:/hotels";
    }

    @GetMapping("/{id}")
    public String getHotelById(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        return "hotels/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        return "hotels/edit";
    }

    @PostMapping("/{id}")
    public String updateHotel(@PathVariable Long id, @Valid @ModelAttribute("hotel") Hotel hotel, BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "hotels/edit";
        }

        hotelService.updateHotel(id, hotel);
        redirectAttributes.addFlashAttribute("message", "Данные отеля успешно обновлены!");
        return "redirect:/hotels";
    }

    @PostMapping("/{id}/delete")
    public String deleteHotel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hotelService.deleteHotel(id);
        redirectAttributes.addFlashAttribute("message", "Отель успешно удалён!");
        return "redirect:/hotels";
    }

    @GetMapping("/search")
    public String searchHotels(@RequestParam("query") String query, Model model) {
        List<Hotel> searchResults = hotelService.searchHotels(query);
        model.addAttribute("hotels", searchResults);
        model.addAttribute("query", query);
        return "hotels/list";
    }
}
