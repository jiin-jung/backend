package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendRepository extends JpaRepository<Attend, Long> {
    boolean existsByUser_UserIdAndDate(Long userId, LocalDate date);
    List<Attend> findByUser_UserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
