package org.nurfet.hotelchain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.model.Room;
import org.nurfet.hotelchain.service.BookingService;
import org.nurfet.hotelchain.service.HotelService;
import org.nurfet.hotelchain.service.RoomService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotels/{hotelId}/rooms")
@Slf4j
public class RoomController {

    private final RoomService roomService;

    private final HotelService hotelService;

    private final BookingService bookingService;


    @GetMapping
    public String getAllRooms(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId).orElseThrow(() -> new NotFoundException(Hotel.class, hotelId));
        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        return "rooms/list";  // шаблон "rooms/list.html"
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable("hotelId") Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new NotFoundException(Hotel.class, hotelId));

        Room room = new Room();
        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);
        return "rooms/new";
    }


    @PostMapping
    public String createRoom(@PathVariable Long hotelId, @ModelAttribute("room") Room room, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/new";
        }
        roomService.createRoom(hotelId, room);
        redirectAttributes.addFlashAttribute("message", "Номер успешно добавлен!");
        return "redirect:/hotels/" + hotelId + "/rooms";
    }

    @GetMapping("/{roomId}")
    public String getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId, Model model) {
        Room room = roomService.getRoomById(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));
        Hotel hotel = hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new NotFoundException(Hotel.class, hotelId));

        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);

        return "rooms/details";
    }

    @GetMapping("/{roomId}/edit")
    public String showEditForm(@PathVariable Long hotelId, @PathVariable Long roomId, Model model) {
        Room room = roomService.getRoomById(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));
        Hotel hotel = hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new NotFoundException(Hotel.class, hotelId));

        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);

        return "rooms/edit";
    }

    @PutMapping("/{roomId}")
    public String updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @ModelAttribute("room") Room room, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/edit";
        }
        roomService.updateRoom(hotelId, roomId, room);
        redirectAttributes.addFlashAttribute("message", "Данные номера успешно обновлены!");
        return "redirect:/hotels/" + hotelId + "/rooms";
    }

    @DeleteMapping("/{roomId}/delete")
    public String deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId, RedirectAttributes redirectAttributes) {

        try {
            roomService.deleteRoom(hotelId, roomId);
            redirectAttributes.addFlashAttribute("message", "Номер успешно удалён!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Нельзя удалить забронированный номер.");
            return "redirect:/hotels/" + hotelId + "/rooms";
        }
        return "redirect:/hotels/" + hotelId + "/rooms";
    }

    @GetMapping("/{roomId}/availability")
    public String checkRoomAvailability(@PathVariable("hotelId") Long hotelId,
                                        @PathVariable("roomId") Long roomId,
                                        Model model) {
        Room room = roomService.getRoomById(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));

        model.addAttribute("room", room);
        model.addAttribute("hotel", room.getHotel());

        return "rooms/availability";
    }

    @PostMapping("/{roomId}/availability")
    public String checkRoomAvailability(@PathVariable("hotelId") Long hotelId,
                                        @PathVariable("roomId") Long roomId,
                                        @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                        @RequestParam("checkInTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkInTime,
                                        @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                        @RequestParam("checkOutTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOutTime,
                                        Model model) {

        Room room = roomService.getRoomById(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));

        boolean isAvailable = bookingService.isRoomAvailable(roomId, checkInDate, checkInTime, checkOutDate, checkOutTime, null);

        model.addAttribute("room", room);
        model.addAttribute("hotel", room.getHotel());
        model.addAttribute("isAvailable", isAvailable);
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);

        return "rooms/availability";
    }


    @GetMapping("/search")
    public String searchRooms(@PathVariable("hotelId") Long hotelId,
                              @RequestParam("category") String category,
                              Model model) {
        List<Room> rooms = roomService.searchByCategory(hotelId, category);
        Hotel hotel = hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new NotFoundException(Room.class, hotelId));

        model.addAttribute("rooms", rooms);
        model.addAttribute("hotel", hotel);
        return "rooms/list";
    }
}
