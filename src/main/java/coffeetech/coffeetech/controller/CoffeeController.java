package coffeetech.coffeetech.controller;


import coffeetech.coffeetech.dto.LoginRequestDto;
import coffeetech.coffeetech.dto.SignupRequestDto;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.jwt.JwtTokenProvider;
import coffeetech.coffeetech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CoffeeController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/coffee/signup")
    public ResponseEntity<?>signup(@RequestBody SignupRequestDto signupRequestDto) {
        boolean userExists = userRepository.findByEmail(signupRequestDto.getEmail()).isPresent();
        if (userExists) {
            return ResponseEntity.status(409).body(
                    Map.of("status", "fail",
                            "message", "이미 존재하는 이메일입니다.")
            );
        }

        User user = new User();
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(signupRequestDto.getPassword());
        user.setNickname(signupRequestDto.getNickname());
        user.setAge(signupRequestDto.getAge());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "회원가입 완료"
        ));
    }


    @PostMapping("/coffee/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .filter(user -> user.getPassword().equals(loginRequestDto.getPassword()))
                .map(user -> {
                    String token = jwtTokenProvider.generateToken(user.getEmail());
                    return ResponseEntity.ok().body(
                            java.util.Map.of(
                                    "status", "success",
                                    "nickname", user.getNickname(),
                                    "token", token
                            )
                    );
                })
                .orElse(ResponseEntity.status(401).body(Map.of(
                        "status", "fail",
                        "message","이메일 또는 비밀번호가 틀렸습니다."
                )));
    }
}
