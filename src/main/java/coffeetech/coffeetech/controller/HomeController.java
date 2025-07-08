package coffeetech.coffeetech.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://coffeeloging.duckdns.org")
@RestController
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "Welcome to CoffeeTech!";
    }
}
