package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
