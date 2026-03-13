package ee.cgi.praktika.restaurant_reservation.service;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationsById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation createReservation(Reservation newRes) {
        validateReservation(newRes);
        return reservationRepository.save(newRes);
    }

    public Reservation updateReservation(Reservation reservation) {
        validateReservation(reservation);
        return reservationRepository.save(reservation);
    }

    private void validateReservation(Reservation reservation) {
        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Broneeringuid ei saa teha minevikku.");
        }

        RestaurantTable table = restaurantTableRepository.findById(reservation.getRestaurantTable().getId())
                .orElseThrow(() -> new RuntimeException("Lauda ei leitud."));

        if (reservation.getNumberOfGuests() > table.getCapacity()) {
            throw new RuntimeException("Külaliste arv ületab laua mahutavuse.");
        }

        List<Reservation> existingReservations = reservationRepository
                .findByRestaurantTableId(table.getId());

        for (Reservation existing : existingReservations) {
            if (reservation.getId() != null && reservation.getId().equals(existing.getId())) {
                continue;
            }

            boolean overlaps = reservation.getStartTime().isBefore(existing.getEndTime()) &&
                    reservation.getEndTime().isAfter(existing.getStartTime());

            if (overlaps) {
                throw new RuntimeException("See laud on valitud ajal juba reserveeritud.");
            }
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}