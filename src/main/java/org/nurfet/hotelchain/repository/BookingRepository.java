package org.nurfet.hotelchain.repository;

import org.nurfet.hotelchain.model.Booking;
import org.nurfet.hotelchain.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.status = 'CONFIRMED' AND " +
            "((b.checkOutDate = :checkInDate AND b.checkOutTime > :checkInTimeMinusBuffer) OR " +
            "(b.checkInDate = :checkOutDate AND b.checkInTime < :checkOutTimePlusBuffer) OR " +
            "(b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkInTimeMinusBuffer") LocalTime checkInTimeMinusBuffer,
                                          @Param("checkOutDate") LocalDate checkOutDate,
                                          @Param("checkOutTimePlusBuffer") LocalTime checkOutTimePlusBuffer);

    List<Booking> findByStatusAndCreatedAtBefore(BookingStatus status, LocalDateTime expirationTime);
}
