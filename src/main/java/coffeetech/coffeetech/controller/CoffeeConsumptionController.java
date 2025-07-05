package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.dto.MonthlyConsumptionResponse;
import coffeetech.coffeetech.service.CoffeeConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee/consumption")
public class CoffeeConsumptionController {

    private final CoffeeConsumptionService coffeeConsumptionService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyConsumptionResponse> getMonthlyConsumption(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        MonthlyConsumptionResponse response =
                coffeeConsumptionService.getMonthlyConsumption(userId, month);
        return ResponseEntity.ok(response);
    }
}
