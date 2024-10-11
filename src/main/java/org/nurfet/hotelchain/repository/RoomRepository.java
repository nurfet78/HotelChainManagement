package org.nurfet.hotelchain.repository;

import org.nurfet.hotelchain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    Optional<Room> findByHotelIdAndId(Long hotelId, Long roomId);

    // Поиск номеров по категории
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND LOWER(r.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Room> findByCategory(@Param("hotelId") Long hotelId, @Param("category") String category);

}
