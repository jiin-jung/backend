// Heatmap, Total Quantity, Most Frequent Coffee API
package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.service.CoffeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/coffee")
@RequiredArgsConstructor
public class CoffeeStatsController {

    private final CoffeeService coffeeService;

    // 1. 히트맵용 날짜별 소비량
    @GetMapping("/heatmap")
    public ResponseEntity<?> getDailyCoffeeCounts(@RequestParam String month, @RequestParam int userId) {
        return ResponseEntity.ok(coffeeService.getDailyCoffeeCounts(month, userId));
    }

    // 2. 한 달 총 소비량
    @GetMapping("/monthly-total")
    public ResponseEntity<?> getMonthlyTotal(@RequestParam String month, @RequestParam int userId) {
        int total = coffeeService.getMonthlyTotalQuantity(month, userId);
        return ResponseEntity.ok(Map.of("month", month, "totalQuantity", total));
    }

    // 3. 한 달 최다 메뉴
    @GetMapping("/top-coffee")
    public ResponseEntity<?> getTopCoffee(@RequestParam String month, @RequestParam int userId) {
        String topCoffee = coffeeService.getMostFrequentCoffeeType(month, userId);
        return ResponseEntity.ok(Map.of("month", month, "topCoffee", topCoffee));
    }
}
