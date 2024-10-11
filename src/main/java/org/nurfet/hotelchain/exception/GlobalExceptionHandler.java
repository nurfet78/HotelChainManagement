package org.nurfet.hotelchain.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.service.RoomService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RoomService roomService;

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error/error");

        mav.addObject("message", ex.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("method", request.getMethod());
        mav.addObject("path", request.getRequestURI());
        mav.addObject("timestamp", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault()).format(Instant.now()));


        return mav;
    }

    @ExceptionHandler(HotelNameAlreadyExistsException.class)
    public ModelAndView handleHotelNameAlreadyExistsException(HotelNameAlreadyExistsException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error/error");

        mav.addObject("message", ex.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("method", request.getMethod());
        mav.addObject("path", request.getRequestURI());
        mav.addObject("timestamp", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault()).format(Instant.now()));

        return mav;
    }
}
