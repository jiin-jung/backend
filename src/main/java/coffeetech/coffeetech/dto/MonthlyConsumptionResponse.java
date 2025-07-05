package coffeetech.coffeetech.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class MonthlyConsumptionResponse {

    private String month;
    private int count;
    private List<String> consumptionDates;
}
