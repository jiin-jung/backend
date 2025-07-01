package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.dto.CoffeeDto;
import coffeetech.coffeetech.service.CoffeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee")
@RequiredArgsConstructor
public class CoffeeController {

    private final CoffeeService coffeeService;

    @PostMapping("/record")
    public ResponseEntity<?> recordCoffee(@RequestBody CoffeeDto dto) {
        try {
            int authenticatedUserId = 1; // 나중에 JWT 연동 시 자동 추출
            coffeeService.saveCoffee(dto, authenticatedUserId);
            return ResponseEntity.ok("커피 소비 기록이 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("입력 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }
}
