package coffeetech.coffeetech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickname;
    private int age;
    private String confirmPassword;
}
