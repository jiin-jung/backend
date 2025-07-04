package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.entity.GeneralNotification;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.service.GeneralNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
