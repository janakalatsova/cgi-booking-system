package ee.cgi.praktika.restaurant_reservation.controller;

import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RestaurantTableController {
    private final RestaurantTableService restaurantService;

    @GetMapping()
    public List<RestaurantTable> getAllRestaurantTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return restaurantService.findAvailableTables(start, end);
    }

    @GetMapping("/{id}")
    public RestaurantTable getRestaurantTableById(@PathVariable Long id) {
        return restaurantService.getRestaurantTableById(id);
    }

    @GetMapping("/search")
    public List<RestaurantTable> getRestaurantTableByCapacity(@RequestParam Integer capacity) {
        return restaurantService.getRestaurantTableByCapacity(capacity);
    }

    @PostMapping()
    public RestaurantTable createRestaurantTable(@RequestBody RestaurantTable restaurantTable) {
        return restaurantService.createRestaurantTable(restaurantTable);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        restaurantService.deleteById(id);
    }

    @PutMapping("/{id}")
    public RestaurantTable updateRestaurantTable(@RequestBody RestaurantTable restaurantTable, @PathVariable Long id) {
        restaurantTable.setId(id);
        return restaurantService.updateRestaurantTable(restaurantTable);
    }
}
