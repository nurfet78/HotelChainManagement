package org.nurfet.hotelchain.impl;

import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.exception.HotelNameAlreadyExistsException;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.repository.HotelRepository;
import org.nurfet.hotelchain.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;


    @Override
    @Transactional
    public Hotel createHotel(Hotel hotel) {
        if (!isHotelNameUnique(hotel.getName())) {
            throw new HotelNameAlreadyExistsException("Отель с таким названием уже существует");
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new NotFoundException(Hotel.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional
    public Hotel updateHotel(Long id, Hotel hotelDetails) {


        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Hotel.class, id));

        hotel.setName(hotelDetails.getName());
        hotel.setLocation(hotelDetails.getLocation());
        hotel.setServices(new ArrayList<>(hotelDetails.getServices())); // Привидение коллекции к изменяемому виду
        hotel.setRating(hotelDetails.getRating());
        hotel.setDescription(hotelDetails.getDescription());

        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new NotFoundException(Hotel.class, id);
        }
        hotelRepository.deleteById(id);
    }

    @Override
    public boolean isHotelNameUnique(String name) {
        return !hotelRepository.existsByName(name);
    }

    @Override
    public List<Hotel> searchHotels(String query) {
        return hotelRepository.searchByNameOrLocation(query);
    }
}
