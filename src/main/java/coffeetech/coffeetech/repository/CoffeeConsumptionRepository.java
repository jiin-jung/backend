package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.CoffeeConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeConsumptionRepository extends JpaRepository<CoffeeConsumption, Long> {
}
