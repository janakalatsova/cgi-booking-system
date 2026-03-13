package ee.cgi.praktika.restaurant_reservation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "x_coordinate")
    private int xCoordinate;

    @Column(name = "y_coordinate")
    private int yCoordinate;

    @Column(name = "is_near_window")
    private boolean isNearWindow;

    @Column(name = "is_quiet")
    private boolean isQuiet;

    @Column(name = "is_kids_friendly")
    private boolean isKidsFriendly;

    @Enumerated(EnumType.STRING)
    @Column(name = "zone")
    private Zone zone;

    @Transient
    private boolean occupied;

    @Transient
    private int searchScore;

    public enum Zone {
        TERRACE,
        PRIVATE,
        C_HALL
    }
}