package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.dto.CoffeeDto;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.jwt.JwtTokenProvider;
import coffeetech.coffeetech.repository.UserRepository;
import coffeetech.coffeetech.service.CoffeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee")
public class CoffeeRecordController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
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

    @GetMapping("/list")
    public ResponseEntity<?> getCoffeeList(@RequestParam String month,
                                           @AuthenticationPrincipal User user) {
        try {
            List<CoffeeDto> list = coffeeService.getCoffeeList(month, user);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("조회 실패: " + e.getMessage());
        }
    }
}
