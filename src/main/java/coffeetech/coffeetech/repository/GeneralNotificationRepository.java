package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.GeneralNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GeneralNotificationRepository extends JpaRepository<GeneralNotification, Long> {
    List<GeneralNotification> findByUserUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime after);
    void deleteAllByCreatedAtBefore(LocalDateTime cutoff);
}
