package coffeetech.coffeetech.repository;


import coffeetech.coffeetech.domain.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
