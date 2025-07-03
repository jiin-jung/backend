package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee/attend")
public class AttendController {

    private final AttendService attendService;

    // 1. 출석 체크 (오늘 날짜로)
    @PostMapping("/check")
    public ResponseEntity<String> checkTodayAttend(@RequestParam Long userId) {
        attendService.checkAttend(userId, LocalDate.now());
        return ResponseEntity.ok("출석 체크 완료");
    }

    // 2. 해당 월 개근 시 포인트 지급
    @PostMapping("/reward")
    public ResponseEntity<String> rewardIfFullAttend(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        attendService.rewardPointsIfFullAttend(userId, month);
        return ResponseEntity.ok("포인트 보상 처리 완료");
    }
}
