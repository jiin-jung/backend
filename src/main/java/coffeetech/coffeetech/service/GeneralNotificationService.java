package coffeetech.coffeetech.service;

import coffeetech.coffeetech.entity.GeneralNotification;
import coffeetech.coffeetech.repository.GeneralNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralNotificationService {

    private final GeneralNotificationRepository repo;

    public List<GeneralNotification> getRecentNotifications(Long userId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return repo.findByUserUserIdAndCreatedAtAfterOrderByCreatedAtDesc(userId, sevenDaysAgo);
    }

}
