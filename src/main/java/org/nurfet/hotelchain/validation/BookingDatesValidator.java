package org.nurfet.hotelchain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.nurfet.hotelchain.model.Booking;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class BookingDatesValidator implements ConstraintValidator<ValidBookingDates, Booking> {

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {

        if (booking == null) {
            return true;
        }

        if (booking.getCheckInDate() == null || booking.getCheckInTime() == null ||
                booking.getCheckOutDate() == null || booking.getCheckOutTime() == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
            return false;
        }

        if (booking.getCheckInDate().isBefore(today)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата заезда не может быть раньше текущей даты.")
                    .addConstraintViolation();
            return false;
        }

        if (!booking.getCheckInDate().isBefore(booking.getCheckOutDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата выезда должна быть позже даты заезда.")
                    .addConstraintViolation();
            return false;
        }

        LocalDateTime checkInDateTime = LocalDateTime.of(booking.getCheckInDate(), booking.getCheckInTime());
        LocalDateTime checkOutDateTime = LocalDateTime.of(booking.getCheckOutDate(), booking.getCheckOutTime());

        Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
        if (duration.toHours() < 24) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Минимальный срок бронирования — 24 часа.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
