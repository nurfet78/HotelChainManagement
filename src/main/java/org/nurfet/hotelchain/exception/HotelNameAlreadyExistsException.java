package org.nurfet.hotelchain.exception;

public class HotelNameAlreadyExistsException extends RuntimeException {
    public HotelNameAlreadyExistsException(String message) {
        super(message);
    }
}
