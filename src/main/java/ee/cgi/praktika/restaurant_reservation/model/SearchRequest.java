package ee.cgi.praktika.restaurant_reservation.model;


import lombok.Data;

@Data
public class SearchRequest {
    private int guests;
    private String startTime;
    private String endTime;
    private String zone;
    private boolean quiet;
    private boolean kids;
    private boolean window;
}
