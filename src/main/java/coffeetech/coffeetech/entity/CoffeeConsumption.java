package coffeetech.coffeetech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coffee_consumption")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coffeeId;

    private int quantity;

    private String coffeeType;

    private LocalDate date;

    private int userId;
}
