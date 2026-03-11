package ee.cgi.praktika.restaurant_reservation.repository;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByNumberOfGuests(int numberOfGuests);
    void deleteById(Long id);
    List<Reservation> findByRestaurantTableId(Long tableId);
    @Query("SELECT r.restaurantTable.id FROM Reservation r WHERE " +
            "(r.startTime < :endTime AND r.endTime > :startTime)")
    List<Long> findOccupiedTableIds(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);
}
