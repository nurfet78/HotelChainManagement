package org.nurfet.hotelchain.service;

import org.nurfet.hotelchain.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAllBookings();

    Optional<Booking> getBookingById(Long id);

    Booking createBooking(Booking booking);

    Booking confirmBooking(Long bookingId) throws Exception;

    Booking cancelBooking(Long bookingId) throws Exception;

    Booking updateBooking(Long bookingId, Booking bookingDetails);

    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalTime checkInTime,
                            LocalDate checkOutDate, LocalTime checkOutTime, Long bookingId);

    void deleteBooking(Long bookingId);

    void cancelExpiredBookings();
}
