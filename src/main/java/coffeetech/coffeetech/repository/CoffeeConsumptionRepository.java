package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.CoffeeConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CoffeeConsumptionRepository extends JpaRepository<CoffeeConsumption, Long> {

    List<CoffeeConsumption> findByUser_UserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
