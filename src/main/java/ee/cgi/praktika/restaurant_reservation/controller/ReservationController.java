package ee.cgi.praktika.restaurant_reservation.controller;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationsById(@PathVariable Long id) {
        return reservationService.getReservationsById(id);
    }

    @GetMapping("/occupied")
    public List<Long> getOccupiedTables(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        return reservationRepository.findOccupiedTableIds(startTime, endTime);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@RequestBody Reservation reservation, @PathVariable Long id) {
        reservation.setId(id);
        return reservationService.updateReservation(reservation);
    }
}