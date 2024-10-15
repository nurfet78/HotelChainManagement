package org.nurfet.hotelchain.impl;

import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.exception.NotFoundException;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.model.Room;
import org.nurfet.hotelchain.repository.HotelRepository;
import org.nurfet.hotelchain.repository.RoomRepository;
import org.nurfet.hotelchain.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;


    @Override
    public Room createRoom(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(Hotel.class, hotelId));

        room.setHotel(hotel); // Связываем номер с отелем
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long hotelId, Long roomId) {
        return roomRepository.findByHotelIdAndId(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));
    }

    @Override
    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public Room updateRoom(Long hotelId, Long roomId, Room roomDetails) {
        // Проверяем, принадлежит ли номер указанному отелю
        Room room = roomRepository.findByHotelIdAndId(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));

        // Обновляем данные номера
        room.setCategory(roomDetails.getCategory());
        room.setPrice(roomDetails.getPrice());
        //room.setStatus(roomDetails.getStatus());
        room.setDescription(roomDetails.getDescription());

        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long hotelId, Long roomId) {
        // Проверяем, принадлежит ли номер указанному отелю
        Room room = roomRepository.findByHotelIdAndId(hotelId, roomId)
                .orElseThrow(() -> new NotFoundException(Room.class, roomId));

        roomRepository.delete(room);
    }


    @Override
    public List<Room> searchByCategory(Long hotelId, String category) {
        return roomRepository.findByCategory(hotelId, category);
    }
}
