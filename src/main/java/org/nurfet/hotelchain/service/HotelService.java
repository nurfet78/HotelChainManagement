package org.nurfet.hotelchain.service;

import org.nurfet.hotelchain.model.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    Optional<Hotel> getHotelById(Long id);

    List<Hotel> getAllHotels();

    Hotel updateHotel(Long id, Hotel hotelDetails);

    void deleteHotel(Long id);

    boolean isHotelNameUnique(String name);

    List<Hotel> searchHotels(String query);
}
