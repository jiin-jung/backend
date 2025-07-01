package coffeetech.coffeetech.controller;

import coffeetech.coffeetech.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/db")
    public String testDB() {
        long count = userRepository.count();
        return "DB 연결 성공! 현재 유저 수: " + count;
    }
}
