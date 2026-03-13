package ee.cgi.praktika.restaurant_reservation.service;

import ee.cgi.praktika.restaurant_reservation.model.Reservation;
import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {
        List<RestaurantTable> allTables = tableRepository.findAll();

        if (allTables.isEmpty()) {
            System.out.println(">>> Andmebaasis pole laudu. Palun lisa lauad kõigepealt!");
            return;
        }

        if (reservationRepository.count() == 0) {
            Random random = new Random();
            System.out.println(">>> Alustan juhuslike broneeringute genereerimist...");

            String[] nimed = {"Mari Tamm", "Jüri Kask", "Kati Sepp", "Andres Mägi", "Tiina Lepik", "Mart Pärn"};

            for (int i = 0; i < 6; i++) {
                RestaurantTable randomTable = allTables.get(random.nextInt(allTables.size()));

                LocalDateTime start = LocalDateTime.now()
                        .withHour(12 + random.nextInt(9))
                        .withMinute(0).withSecond(0).withNano(0);
                LocalDateTime end = start.plusHours(2);

                Reservation res = new Reservation();
                res.setCustomerName(nimed[i % nimed.length]);
                res.setCustomerPhone("+372 5" + (100000 + random.nextInt(900000)));
                res.setNumberOfGuests(random.nextInt(randomTable.getCapacity()) + 1);
                res.setStartTime(start);
                res.setEndTime(end);
                res.setRestaurantTable(randomTable);

                reservationRepository.save(res);
            }
            System.out.println(">>> Edukalt loodud 6 juhuslikku broneeringut.");
        } else {
            System.out.println(">>> Broneeringud on juba olemas, uusi ei genereerita.");
        }
    }
}