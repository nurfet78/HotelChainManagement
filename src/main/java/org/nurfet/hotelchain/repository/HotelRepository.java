package org.nurfet.hotelchain.repository;

import org.nurfet.hotelchain.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsByName(String name);

    // Запрос для поиска отелей по части названия или локации
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(h.location) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Hotel> searchByNameOrLocation(@Param("query") String query);
}
