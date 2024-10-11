package org.nurfet.hotelchain.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Booking;
import org.nurfet.hotelchain.model.BookingStatus;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.model.Room;
import org.nurfet.hotelchain.repository.BookingRepository;
import org.nurfet.hotelchain.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {

        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmBooking(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Бронирование не найдено"));

        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Бронирование не найдено"));

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Long bookingId, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class, bookingId));

        Room room = booking.getRoom();

        if (isRoomAvailable(room.getId(), bookingDetails.getCheckInDate(), bookingDetails.getCheckInTime(),
                bookingDetails.getCheckOutDate(), bookingDetails.getCheckOutTime(), bookingId)) {
            throw new IllegalStateException("Номер недоступен на новые даты и время.");
        }
        
        booking.setCheckInDate(bookingDetails.getCheckInDate());
        booking.setCheckInTime(bookingDetails.getCheckInTime());
        booking.setCheckOutDate(bookingDetails.getCheckOutDate());
        booking.setCheckOutTime(bookingDetails.getCheckOutTime());
        return bookingRepository.save(booking);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalTime checkInTime,
                                   LocalDate checkOutDate, LocalTime checkOutTime, Long bookingId) {

        LocalTime checkInTimeMinusBuffer = checkInTime.minusHours(2);
        LocalTime checkOutTimePlusBuffer = checkOutTime.plusHours(2);

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                roomId, checkInDate, checkInTimeMinusBuffer, checkOutDate, checkOutTimePlusBuffer);

        return conflictingBookings.stream().anyMatch(booking -> !booking.getId().equals(bookingId));
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }


    // Поиск просроченных бронирований
    @Override
    @Scheduled(fixedRate = 300000)
    public void cancelExpiredBookings() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5); // 5 минут для теста
        List<Booking> expiredBookings = bookingRepository.findByStatusAndCreatedAtBefore(BookingStatus.PENDING, expirationTime);
        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        }
    }
}
























