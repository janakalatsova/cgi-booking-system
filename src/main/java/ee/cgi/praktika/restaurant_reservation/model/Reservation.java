package ee.cgi.praktika.restaurant_reservation.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    private String customerName;
    private String customerPhone;
    private Integer numberOfGuests;

    @Column(length = 1000)
    private String specialRequests;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;
}