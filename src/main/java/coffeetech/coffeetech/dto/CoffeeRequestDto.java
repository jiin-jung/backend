package coffeetech.coffeetech.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CoffeeRequestDto {

    private int price;
    private int quantity;
    private String coffeeType;
    private LocalDate date;
    private int userId;
}
