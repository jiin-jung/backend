package coffeetech.coffeetech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class MonthlyAttendResponse {
    private String month;
    private List<Integer> attendedDays;
    private boolean isFullAttend;
    private int remainDays;
}
