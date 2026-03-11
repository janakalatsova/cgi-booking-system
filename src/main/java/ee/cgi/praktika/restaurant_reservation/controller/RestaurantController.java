package ee.cgi.praktika.restaurant_reservation.controller;

import ee.cgi.praktika.restaurant_reservation.model.RestaurantTable;
import ee.cgi.praktika.restaurant_reservation.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping()
    public List<RestaurantTable> getAllRestaurantTables() {
        return restaurantService.getAllRestaurantTables();
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
