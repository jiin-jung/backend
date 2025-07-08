package coffeetech.coffeetech.service;

import coffeetech.coffeetech.dto.CoffeeDto;
import coffeetech.coffeetech.entity.Attend;
import coffeetech.coffeetech.entity.Coffee;
import coffeetech.coffeetech.entity.CoffeeConsumption;
import coffeetech.coffeetech.entity.User;
import coffeetech.coffeetech.repository.AttendRepository;
import coffeetech.coffeetech.repository.CoffeeConsumptionRepository;
import coffeetech.coffeetech.repository.CoffeeRepository;
import coffeetech.coffeetech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final CoffeeConsumptionRepository coffeeConsumptionRepository;

    public List<Coffee> getRecentCoffeeMenu() {
        return coffeeRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public void saveCoffee(CoffeeDto dto, long userId) {
        CoffeeConsumption coffee = new CoffeeConsumption();

        try {
            coffee.setDate(LocalDate.parse(dto.getDate())); // 날짜 파싱
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("날짜 형식이 잘못되었거나 누락되었습니다. yyyy-MM-dd 형식으로 입력해주세요.");
        }

        coffee.setQuantity(dto.getQuantity());
        coffee.setCoffeeType(dto.getCoffeeType());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        coffee.setUser(user);

        coffeeConsumptionRepository.save(coffee);

        if (!attendRepository.existsByUser_UserIdAndDate(user.getUserId(), coffee.getDate())) {
            Attend attend = new Attend();
            attend.setDate(coffee.getDate());
            attend.setUser(user);
            attendRepository.save(attend);
        }
    }

    public void checkAndRewardAttendance(Long userId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        List<Attend> attends = attendRepository.findByUser_UserIdAndDateBetween(userId, start, end);

        if (attends.size() == end.getDayOfMonth()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.addPoints(100); // 예시 포인트
            userRepository.save(user);
        }
    }

    public Map<String, Integer> getDailyCoffeeCounts(String month, int userId) {
        List<CoffeeConsumption> records = coffeeConsumptionRepository.findByUserIdAndMonth(userId, month);

        System.out.println(">>> 조회된 레코드 수: " + records.size());
        for (CoffeeConsumption c : records) {
            System.out.println(">>> 커피: " + c.getCoffeeType() + ", 수량: " + c.getQuantity() + ", 날짜: " + c.getDate());
        }

        Map<String, Integer> result = new HashMap<>();
        for (CoffeeConsumption record : records) {
            String date = record.getDate().toString(); // yyyy-MM-dd
            result.put(date, result.getOrDefault(date, 0) + record.getQuantity());
        }

        return result;
    }

    // 2. 해당 월 총 소비 잔수
    public int getMonthlyTotalQuantity(String month, int userId) {
        return coffeeConsumptionRepository.findByUserIdAndMonth(userId, month)
                .stream()
                .mapToInt(CoffeeConsumption::getQuantity)
                .sum();
    }

    // 3. 가장 자주 마신 메뉴
    public String getMostFrequentCoffeeType(String month, int userId) {
        List<CoffeeConsumption> records = coffeeConsumptionRepository.findByUserIdAndMonth(userId, month);
        Map<String, Integer> countMap = new HashMap<>();
        for (CoffeeConsumption r : records) {
            countMap.put(r.getCoffeeType(), countMap.getOrDefault(r.getCoffeeType(), 0) + r.getQuantity());
        }
        return countMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("데이터 없음");
    }

    public List<CoffeeDto> getCoffeeList(String month, User user) {
        YearMonth ym = YearMonth.parse(month); // "2025-07"
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<CoffeeConsumption> records =
                coffeeConsumptionRepository.findByUser_UserIdAndDateBetween(user.getUserId(), start, end);

        return records.stream()
                .map(CoffeeDto::fromEntity)
                .collect(Collectors.toList());
    }
}