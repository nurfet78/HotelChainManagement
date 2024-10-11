package org.nurfet.hotelchain.service;

import org.nurfet.hotelchain.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {


    Room createRoom(Long hotelId, Room room);

    Optional<Room> getRoomById(Long hotelId, Long roomId);

    List<Room> getRoomsByHotelId(Long hotelId);

    Room updateRoom(Long hotelId, Long roomId, Room roomDetails);

    void deleteRoom(Long hotelId, Long roomId);

    List<Room> searchByCategory(Long hotelId, String category);
}
