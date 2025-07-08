package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.CoffeeConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CoffeeConsumptionRepository extends JpaRepository<CoffeeConsumption, Long> {

    List<CoffeeConsumption> findByUser_UserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 👇 이 메서드가 정의되어 있어야 합니다!
    @Query(value = "SELECT * FROM coffee_consumption WHERE user_id = :userId AND DATE_FORMAT(date, '%Y-%m') = :month", nativeQuery = true)
    List<CoffeeConsumption> findByUserIdAndMonth(@Param("userId") long userId, @Param("month") String month);
}
