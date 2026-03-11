package ee.cgi.praktika.restaurant_reservation.repository;

import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByCapacity(int capacity);
    void deleteById(Long id);
    RestaurantTable getByCapacity(int capacity);
}
