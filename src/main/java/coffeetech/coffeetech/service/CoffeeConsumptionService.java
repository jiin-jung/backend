package coffeetech.coffeetech.service;


import coffeetech.coffeetech.dto.MonthlyConsumptionResponse;
import coffeetech.coffeetech.entity.CoffeeConsumption;
import coffeetech.coffeetech.repository.CoffeeConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoffeeConsumptionService {

    private final CoffeeConsumptionRepository consumptionRepository;

    public MonthlyConsumptionResponse getMonthlyConsumption(Long userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<CoffeeConsumption> consumptions =
                consumptionRepository.findByUser_UserIdAndDateBetween(userId, start, end);

        List<String> dates = consumptions.stream()
                .map(c -> c.getDate().toString())
                .collect(Collectors.toList());

        return new MonthlyConsumptionResponse(
                yearMonth.toString(),
                dates.size(),
                dates
        );
    }
}
