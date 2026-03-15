package ee.cgi.praktika.restaurant_reservation.service;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private RestaurantTable savedTable;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        tableRepository.deleteAll();
        savedTable = tableRepository.save(new RestaurantTable(null, 4, 0, 0, false, false, false, RestaurantTable.Zone.C_HALL, false, 0));
    }


    @Test
    void createReservation_Success() {
        Reservation res = new Reservation(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2),
                "Ivan", "555", 2, "Window plz", savedTable);
        Reservation saved = reservationService.createReservation(res);
        assertNotNull(saved.getId());
    }

    @Test
    void createReservation_Fail_PastTime() {
        Reservation res = new Reservation(null, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(2),
                "Ivan", "555", 2, "", savedTable);

        Exception ex = assertThrows(RuntimeException.class, () -> reservationService.createReservation(res));
        assertEquals("Broneeringuid ei saa teha minevikku.", ex.getMessage());
    }

    @Test
    void createReservation_Fail_OverCapacity() {
        Reservation res = new Reservation(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2),
                "Ivan", "555", 10, "", savedTable); // Стол на 4, а гостей 10

        Exception ex = assertThrows(RuntimeException.class, () -> reservationService.createReservation(res));
        assertEquals("Külaliste arv ületab laua mahutavuse.", ex.getMessage());
    }

    @Test
    void createReservation_Fail_Overlap() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        reservationService.createReservation(new Reservation(null, start, end, "User 1", "1", 2, "", savedTable));

        Reservation overlap = new Reservation(null, start.plusHours(1), end.plusHours(1), "User 2", "2", 2, "", savedTable);

        assertThrows(RuntimeException.class, () -> reservationService.createReservation(overlap));
    }

    @Test
    void updateReservation_ShouldChangeDetails() {
        Reservation res = reservationService.createReservation(new Reservation(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2), "Old Name", "1", 2, "", savedTable));

        res.setCustomerName("New Name");
        Reservation updated = reservationService.updateReservation(res);
        assertEquals("New Name", updated.getCustomerName());
    }

    @Test
    void getAllAndGetById() {
        reservationService.createReservation(new Reservation(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2), "A", "1", 2, "", savedTable));

        assertEquals(1, reservationService.getAllReservations().size());
    }

    @Test
    void deleteReservation() {
        Reservation res = reservationService.createReservation(new Reservation(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2), "A", "1", 2, "", savedTable));

        reservationService.deleteById(res.getId());
        assertNull(reservationService.getReservationsById(res.getId()));
    }
}