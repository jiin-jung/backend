package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.entity.GeneralNotification;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.service.GeneralNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/coffee/notifications")
@RequiredArgsConstructor
public class GeneralNotificationController {

    private final GeneralNotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<GeneralNotification>> getUserNotifications(
            @AuthenticationPrincipal User user) {

        Long userId = user.getUserId();
        List<GeneralNotification> notifications = notificationService.getRecentNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
}
