package org.nurfet.hotelchain.exception;

public class NotFoundException extends RuntimeException{

    public <T> NotFoundException(Class<T> cls, Long id) {
        super(cls.getName() + " сid " + id + " не найден");
    }
}
