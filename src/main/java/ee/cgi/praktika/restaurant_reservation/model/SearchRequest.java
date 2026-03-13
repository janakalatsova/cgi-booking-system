package ee.cgi.praktika.restaurant_reservation.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchRequest {
    private int guests;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    private String zone;
    private boolean isQuiet;
    private boolean isKidsFriendly;
    private boolean isNearWindow;
}
