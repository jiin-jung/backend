package coffeetech.coffeetech.controller;


import coffeetech.coffeetech.dto.LoginRequestDto;
import coffeetech.coffeetech.dto.SignupRequestDto;
import coffeetech.coffeetech.entity.Coffee;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.jwt.JwtTokenProvider;
import coffeetech.coffeetech.repository.UserRepository;
import coffeetech.coffeetech.service.CoffeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee")
public class CoffeeController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CoffeeService coffeeService;

    // 회원 가입 컨트롤러입니다.
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        // 이메일 중복 확인 코드
        boolean userExists = userRepository.findByEmail(signupRequestDto.getEmail()).isPresent();
        if (userExists) {
            return ResponseEntity.status(409).body(
                    Map.of("status", "fail",
                            "message", "이미 존재하는 이메일입니다.")
            );
        }

        // 비밀번호 확인 불일치 검사 코드
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())) {
            return ResponseEntity.status(409).body(
                    Map.of("status", "fail",
                            "message", "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
            );
        }

        // 비밀번호 조건 정규식 검증 코드
        String password = signupRequestDto.getPassword();
        if (!isValidPassword(password)) {
            return ResponseEntity.status(409).body(
                    Map.of("status", "fail",
                            "message", "비밀번호는 8자리 이상이며, 영문/숫자/특수문자를 모두 포함해야 합니다.")
            );
        }

        // 별명 조건 정규식 검증 코드
        if (!isValidNickname(signupRequestDto.getNickname())) {
            return ResponseEntity.status(409).body(
                    Map.of("status", "fail",
                            "message", "닉네임은 국문 2~5자 또는 영문/숫자 3~7자만 사용할 수 있습니다.")
            );
        }


        User user = new User();
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(signupRequestDto.getNickname());
        user.setAge(signupRequestDto.getAge());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "회원가입 완료"
        ));

    }

    // 비밀번호 유효성 체크 코드
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$";
        return password != null && password.matches(regex);
    }

    // 별명 유효성 체크 코드
    private boolean isValidNickname(String nickname) {
        if (nickname == null) return false;

        // 국문: 2~5자, 한글만
        if (nickname.matches("^[가-힣]{2,5}$")) return true;

        // 영문: 3~7자, 알파벳만 (대소문자), 숫자 포함 가능
        return nickname.matches("^[a-zA-Z0-9]{3,7}$");
    }

    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        boolean exists = userRepository.findByEmail(email).isPresent();


        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .filter(user -> passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()))  // ✅ 여기 중요
                .map(user -> {
                    String token = jwtTokenProvider.generateToken(user.getEmail());
                    return ResponseEntity.ok().body(
                            Map.of(
                                    "status", "success",
                                    "nickname", user.getNickname(),
                                    "token", token
                            )
                    );
                })
                .orElse(ResponseEntity.status(401).body(Map.of(
                        "status", "fail",
                        "message", "이메일 또는 비밀번호가 틀렸습니다."
                )));
    }

    @GetMapping("/recent-coffee")
    public ResponseEntity<List<Coffee>> getRecentCoffeeMenu() {
        List<Coffee> recentList = coffeeService.getRecentCoffeeMenu();
        return ResponseEntity.ok(recentList);
    }
}
