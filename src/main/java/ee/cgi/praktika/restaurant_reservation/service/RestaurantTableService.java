package ee.cgi.praktika.restaurant_reservation.service;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.model.SearchRequest;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    public List<RestaurantTable> findBestTables(SearchRequest request) {
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();
        List<RestaurantTable> allTables = restaurantTableRepository.findAll();
        List<Reservation> allReservations = reservationRepository.findAll();

        return allTables.stream()
                .filter(t -> t.getCapacity() >= request.getGuests())
                .filter(t -> request.getZone() == null || request.getZone().isEmpty() ||
                        t.getZone().toString().equals(request.getZone()))
                .filter(t -> isTableAvailable(t, start, end, allReservations))
                .peek(t -> {
                    int score = 0;
                    if (t.getCapacity() == request.getGuests()) score += 10;
                    if (request.isQuiet() && t.isQuiet()) score += 5;
                    if (request.isNearWindow() && t.isNearWindow()) score += 5;
                    if (request.isKidsFriendly() && t.isKidsFriendly()) score += 5;
                    t.setSearchScore(score);
                })
                .sorted(Comparator.comparingInt(RestaurantTable::getSearchScore).reversed())
                .toList();
    }

    public RestaurantTable getRestaurantTableById(Long id) {
        return restaurantTableRepository.findById(id).orElse(null);
    }

    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public RestaurantTable createRestaurantTable(RestaurantTable restaurantTable) {
        return restaurantTableRepository.save(restaurantTable);
    }

    public List<RestaurantTable> findAvailableTables(LocalDateTime start, LocalDateTime end) {
        List<RestaurantTable> allTables = restaurantTableRepository.findAll();

        List<Reservation> allReservations = reservationRepository.findAll();

        for (RestaurantTable table : allTables) {
            boolean available = isTableAvailable(table, start, end, allReservations);
            table.setOccupied(!available);
        }

        return allTables;
    }

    private boolean isTableAvailable(RestaurantTable table, LocalDateTime start, LocalDateTime end, List<Reservation> reservations) {
        return reservations.stream()
                .filter(r -> r.getRestaurantTable().getId().equals(table.getId()))
                .noneMatch(r -> start.isBefore(r.getEndTime()) && end.isAfter(r.getStartTime()));
    }

    public RestaurantTable updateRestaurantTable(RestaurantTable restaurantTable) {
        return restaurantTableRepository.save(restaurantTable);
    }

    public void deleteById(Long id) {
        restaurantTableRepository.deleteById(id);
    }
}
