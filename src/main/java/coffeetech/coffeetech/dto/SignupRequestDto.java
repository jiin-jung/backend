package coffeetech.coffeetech.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickname;
    private int age;
}
