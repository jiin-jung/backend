package coffeetech.coffeetech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FullAttendCheckResponse {
    private boolean rewardAvailable;
    private String message;
}
