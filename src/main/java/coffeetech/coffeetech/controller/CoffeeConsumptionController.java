package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.dto.MonthlyConsumptionResponse;
import coffeetech.coffeetech.service.CoffeeConsumptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@Slf4j
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
