package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.CoffeeConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<CoffeeConsumption, Long> {

    @Query(value = "SELECT * FROM coffee_consumption WHERE user_id = :userId AND DATE_FORMAT(date, '%Y-%m') = :month", nativeQuery = true)
    List<CoffeeConsumption> findByUserIdAndMonth(@Param("userId") int userId, @Param("month") String month);
}
