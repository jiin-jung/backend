package coffeetech.coffeetech.jwt;

import coffeetech.coffeetech.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private static final List<String> WHITELIST_PREFIXES = List.of(
            "/",                 // 루트 경로
            "/index.html",       // index.html
            "/api/coffee/login",
            "/api/coffee/signup",
            "/api/coffee/check-email",
            "/static/",          // 정적 파일 경로
            "/css/",             // CSS 경로
            "/js/",              // JS 경로
            "/images/",          // 이미지 경로
            "/favicon.ico"       // 파비콘
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        String path = request.getRequestURI();

        System.out.println("요청 경로: " + path);  // 반드시 로그 찍어서 확인

        if (path.equals("/") || WHITELIST_PREFIXES.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 존재 + 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            System.out.println("🔐 받은 토큰: " + token);
            System.out.println("📧 토큰에서 추출한 이메일: " + email);

            userRepository.findByEmail(email).ifPresentOrElse(user -> {
                System.out.println("✅ DB에서 유저 찾음: " + user.getEmail());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("🔓 인증 주입 성공: " + authentication.isAuthenticated());
            }, () -> {
                System.out.println("❌ DB에 해당 이메일 유저 없음: " + email);
            });
        } else {
            if (token == null) {
                System.out.println("🚫 Authorization 헤더 없음");
            } else {
                System.out.println("🚫 토큰 유효성 검사 실패");
            }
        }

        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 제외
        }
        return null;
    }
}
