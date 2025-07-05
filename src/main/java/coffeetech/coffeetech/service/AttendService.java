package coffeetech.coffeetech.service;

import coffeetech.coffeetech.dto.FullAttendCheckResponse;
import coffeetech.coffeetech.dto.MonthlyAttendResponse;
import coffeetech.coffeetech.entity.Attend;
import coffeetech.coffeetech.entity.Gifticon;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.repository.AttendRepository;
import coffeetech.coffeetech.repository.GifticonRepository;
import coffeetech.coffeetech.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendService {

    private final AttendRepository attendRepository;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;

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

    public FullAttendCheckResponse rewardGifticonIfFullAttend(Long userId, YearMonth month) {
        if (!isFullMonthAttend(userId, month)) {
            return new FullAttendCheckResponse(false, "아직 개근 조건을 충족하지 않았습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Gifticon gifticon = gifticonRepository.findFirstByUserIsNull()
                .orElseThrow(() -> new RuntimeException("지급 가능한 기프티콘이 없습니다."));

        gifticon.setUser(user);
        gifticonRepository.save(gifticon);
        return new FullAttendCheckResponse(true, "기프티콘이 지급되었습니다!");
    }

    public MonthlyAttendResponse getMonthlyAttendInfo(Long userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Attend> attends = attendRepository.findByUser_UserIdAndDateBetween(userId, start, end);
        List<Integer> attendedDays = attends.stream()
                .map(a -> a.getDate().getDayOfMonth())
                .collect(Collectors.toList());

        int totalDays = yearMonth.lengthOfMonth();
        boolean isFull = attendedDays.size() == totalDays;
        int remain = totalDays - attendedDays.size();

        return new MonthlyAttendResponse(
                yearMonth.toString(),
                attendedDays,
                isFull,
                remain
        );
    }

    public FullAttendCheckResponse checkRewardAvailable(Long userId, YearMonth month) {
        boolean isFull = isFullMonthAttend(userId, month);
        String message = isFull ? "개근 보상을 받을 수 있습니다!" : "아직 개근 조건을 충족하지 않았습니다.";
        return new FullAttendCheckResponse(isFull, message);
    }
}
