package org.nurfet.hotelchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Setter;
import org.nurfet.hotelchain.validation.ValidBookingDates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ValidBookingDates
public class Booking extends AbstractEntity {

    @NotNull(message = "Дата въезда должна быть указана")
    private LocalDate checkInDate;

    @NotNull(message = "Дата выезда должна быть указана")
    private LocalDate checkOutDate;

    @NotNull(message = "Время въезда должно быть указано")
    private LocalTime checkInTime;

    @NotNull(message = "Время выезда должно быть указано")
    private LocalTime checkOutTime;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
