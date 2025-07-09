package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.dto.FullAttendCheckResponse;
import coffeetech.coffeetech.dto.MonthlyAttendResponse;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee/attend")
public class AttendController {

    private final AttendService attendService;

    // 출석 체크 (오늘 날짜로)
    @PostMapping("/check")
    public ResponseEntity<String> checkTodayAttend(@AuthenticationPrincipal User user) {
        attendService.checkAttend(user.getUserId(), LocalDate.now());
        return ResponseEntity.ok("출석 체크 완료");
    }

    // 해당 월 개근 시 포인트 지급
    @PostMapping("/reward")
    public ResponseEntity<FullAttendCheckResponse> rewardIfFullAttend(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        FullAttendCheckResponse result = attendService.rewardGifticonIfFullAttend(user.getUserId(), month);
        return ResponseEntity.ok(result);
    }

    // 한 달 출석 현황 조회
    @GetMapping("/calendar")
    public ResponseEntity<MonthlyAttendResponse> getMonthlyAttendStatus(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        MonthlyAttendResponse response = attendService.getMonthlyAttendInfo(userId, month);
        return ResponseEntity.ok(response);
    }

    // 개근 달성 시 팝업 조건 확인
    @GetMapping("/full-attend-check")
    public ResponseEntity<FullAttendCheckResponse> checkFullAttendStatus(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        FullAttendCheckResponse res = attendService.checkRewardAvailable(userId, month);
        return ResponseEntity.ok(res);
    }

}
