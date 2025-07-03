package coffeetech.coffeetech.service;

import coffeetech.coffeetech.entity.Attend;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.repository.AttendRepository;
import coffeetech.coffeetech.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendService {

    private final AttendRepository attendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void checkAttend(Long userId, LocalDate date) {
        if (attendRepository.existsByUser_UserIdAndDate(userId, date)) {
            return; // 이미 출석 기록이 있음
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attend attend = new Attend();
        attend.setUser(user);
        attend.setDate(date);
        attendRepository.save(attend);
    }

    public boolean isFullMonthAttend(Long userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Attend> attendances = attendRepository.findByUser_UserIdAndDateBetween(userId, start, end);

        return attendances.size() == yearMonth.lengthOfMonth();
    }

    public void rewardPointsIfFullAttend(Long userId, YearMonth month) {
        if (isFullMonthAttend(userId, month)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.addPoints(1000);
            userRepository.save(user);
        }
    }
}
