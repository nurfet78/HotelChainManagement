package org.nurfet.hotelchain.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Booking;
import org.nurfet.hotelchain.model.User;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.model.Room;
import org.nurfet.hotelchain.service.BookingService;
import org.nurfet.hotelchain.service.HotelService;
import org.nurfet.hotelchain.service.UserService;
import org.nurfet.hotelchain.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;
import java.time.LocalDate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    private final RoomService roomService;

    private final UserService userService;

    private final HotelService hotelService;

    @GetMapping("/new/{hotelId}/{roomId}")
    public String showCreateForm(@PathVariable Long hotelId, @PathVariable Long roomId, Model model) {

        List<User> users = userService.findAllUsers();

        Booking booking = new Booking();
        booking.setRoom(roomService.getRoomById(hotelId, roomId).orElseThrow(() -> new NotFoundException(Room.class, roomId)));

        model.addAttribute("booking", booking);
        model.addAttribute("users", users);

        return "bookings/create";
    }

    @PostMapping("/create/{hotelId}/{roomId}")
    public String createBooking(@PathVariable Long hotelId, @PathVariable Long roomId,
                                @RequestParam(required = false) Long guestId,
                                Principal principal,
                                @ModelAttribute @Valid Booking booking, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            log.info("Сообщение валидатора: {}", result.getFieldError());
            List<User> users = userService.findAllUsers();
            booking.setRoom(roomService.getRoomById(hotelId, roomId)
                    .orElseThrow(() -> new NotFoundException(Room.class, roomId)));
            model.addAttribute("booking", booking);
            model.addAttribute("guests", users);
            return "bookings/create";
        }

        try {
            Room room = roomService.getRoomById(hotelId, roomId)
                    .orElseThrow(() -> new NotFoundException(Room.class, roomId));

            User user;
            if (guestId != null) {
                user = userService.findUserById(guestId); //пользователь выбранный админом
            } else {
                user = userService.findByUsername(principal.getName()); //авторизованный пользователь
            }

            booking.setRoom(room);
            booking.setUser(user);

            if (bookingService.isRoomAvailable(room.getId(), booking.getCheckInDate(), booking.getCheckInTime(),
                    booking.getCheckOutDate(), booking.getCheckOutTime(), null)) {
                redirectAttributes.addFlashAttribute("error", "Номер уже занят на указанные даты и время.");
                return "redirect:/bookings/new/" + hotelId + "/" + roomId;
            }

            bookingService.createBooking(booking);
            redirectAttributes.addFlashAttribute("message", "Нужно подтверждение");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "bookings/create";
        }
        return "redirect:/bookings";
    }


    @GetMapping("/{id}/confirm")
    public String showConfirmForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id).orElseThrow(() -> new NotFoundException(Booking.class, id));
        model.addAttribute("booking", booking);
        return "bookings/confirm";
    }


    @PostMapping("/{id}/confirm")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("message", "Бронирование подтверждено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/{id}/confirm";
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/cancel")
    public String showCancelForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id).orElseThrow(() -> new NotFoundException(Booking.class, id));
        model.addAttribute("booking", booking);
        return "bookings/cancel";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("message", "Бронирование отменено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/{id}/cancel";
        }
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id).orElseThrow(() -> new NotFoundException(Booking.class, id));

        model.addAttribute("booking", booking);
        return "bookings/edit";
    }


    @PutMapping("/{id}/edit")
    public String updateBooking(@PathVariable("id") Long bookingId,
                                @Valid @ModelAttribute("booking") Booking bookingDetails,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            Booking booking = bookingService.getBookingById(bookingId).orElseThrow(() -> new NotFoundException(Booking.class, bookingId));

            redirectAttributes.addFlashAttribute("booking", booking);
            return "bookings/edit";
        }

        try {
            bookingService.updateBooking(bookingId, bookingDetails);
            redirectAttributes.addFlashAttribute("message", "Бронирование успешно обновлено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/" + bookingId + "/edit";
        }
        return "redirect:/bookings";
    }

    @GetMapping
    public String listBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();

        if (!bookings.isEmpty()) {
            Hotel hotel = bookings.getFirst().getRoom().getHotel();
            model.addAttribute("hotel", hotel);
        }

        model.addAttribute("bookings", bookings);
        return "bookings/list";
    }


    @DeleteMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);

        return "redirect:/bookings";
    }
}
