package org.nurfet.hotelchain.model;

import lombok.Getter;

@Getter
public enum BookingStatus {

    PENDING("Ожидается"),
    CONFIRMED("Подтверждено"),
    CANCELLED("Отменено");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}
