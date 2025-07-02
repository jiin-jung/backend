package coffeetech.coffeetech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoffeeDto {
    private String date;         // 사용자 입력 날짜 (yyyy-MM-dd)
    private String coffeeType;   // 메뉴 이름
    private int quantity;        // 수량 (잔 수)
}

