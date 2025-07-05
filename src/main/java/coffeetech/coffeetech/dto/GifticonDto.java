package coffeetech.coffeetech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GifticonDto {
    private String itemName;
    private String brand;
    private String barcode;
    private LocalDate expireDate;
}
