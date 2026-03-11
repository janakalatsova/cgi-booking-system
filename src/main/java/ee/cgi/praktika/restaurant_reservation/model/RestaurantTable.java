package ee.cgi.praktika.restaurant_reservation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int capacity;
    private int xCoordinate;
    private int yCoordinate;
    private boolean nearWindow;
    private boolean isQuiet;
    private boolean isKidsFriendly;
    @Enumerated(EnumType.STRING)
    private Zone zone;
    @Transient
    private boolean occupied;

    public enum Zone {
        TERRACE,
        PRIVATE,
        C_HALL
    }
}
