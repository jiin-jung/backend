package coffeetech.coffeetech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "gifticon")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gifticon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private String brand;
    private String barcode;
    private LocalDate expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
