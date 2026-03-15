package ee.cgi.praktika.restaurant_reservation.service;

import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.model.SearchRequest;
import ee.cgi.praktika.restaurant_reservation.repository.ReservationRepository;
import ee.cgi.praktika.restaurant_reservation.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RestaurantTableServiceTest {

    @Autowired
    private RestaurantTableService tableService;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        tableRepository.deleteAll();

        tableRepository.save(new RestaurantTable(null, 2, 0, 0, true, false, false, RestaurantTable.Zone.C_HALL, false, 0));
        tableRepository.save(new RestaurantTable(null, 4, 10, 10, false, true, true, RestaurantTable.Zone.PRIVATE, false, 0));
        tableRepository.save(new RestaurantTable(null, 6, 20, 20, false, false, false, RestaurantTable.Zone.TERRACE, false, 0));
    }

    @Test
    void findBestTables_FilterByGuests() {
        SearchRequest request = new SearchRequest();
        request.setGuests(5);
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        List<RestaurantTable> results = tableService.findBestTables(request);

        assertEquals(1, results.size());
        assertEquals(6, results.get(0).getCapacity());
    }

    @Test
    void findBestTables_FilterByZone() {
        SearchRequest request = new SearchRequest();
        request.setGuests(2);
        request.setZone("PRIVATE");

        List<RestaurantTable> results = tableService.findBestTables(request);

        assertEquals(1, results.size());
        assertEquals(RestaurantTable.Zone.PRIVATE, results.get(0).getZone());
    }

    @Test
    void getAllTables_ShouldReturnAll() {
        List<RestaurantTable> tables = tableService.getAllTables();
        assertEquals(3, tables.size());
    }

    @Test
    void getRestaurantTableById_ExistentAndNonExistent() {
        RestaurantTable table = tableService.getAllTables().get(0);
        assertNotNull(tableService.getRestaurantTableById(table.getId()));
        assertNull(tableService.getRestaurantTableById(999L));
    }

    @Test
    void createRestaurantTable_ShouldSave() {
        RestaurantTable newTable = new RestaurantTable(null, 8, 50, 50, false, false, false, RestaurantTable.Zone.C_HALL, false, 0);
        RestaurantTable saved = tableService.createRestaurantTable(newTable);
        assertNotNull(saved.getId());
        assertEquals(8, tableService.getRestaurantTableById(saved.getId()).getCapacity());
    }

    @Test
    void updateRestaurantTable_ShouldChangeData() {
        RestaurantTable table = tableService.getAllTables().get(0);
        table.setCapacity(10);
        RestaurantTable updated = tableService.updateRestaurantTable(table);
        assertEquals(10, updated.getCapacity());
    }

    @Test
    void deleteById_ShouldRemoveTable() {
        RestaurantTable table = tableService.getAllTables().get(0);
        Long id = table.getId();
        tableService.deleteById(id);
        assertNull(tableService.getRestaurantTableById(id));
    }

    @Test
    void findAvailableTables_ShouldSetOccupiedStatus() {
        List<RestaurantTable> tables = tableService.findAvailableTables(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertNotNull(tables);
        assertFalse(tables.get(0).isOccupied());
    }
}