package coffeetech.coffeetech.service;

import coffeetech.coffeetech.dto.CoffeeDto;
import coffeetech.coffeetech.entity.CoffeeConsumption;
import coffeetech.coffeetech.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public void saveCoffee(CoffeeDto dto, int userId) {
        CoffeeConsumption coffee = new CoffeeConsumption();
        coffee.setCoffeeType(dto.getCoffeeType());
        coffee.setQuantity(dto.getQuantity());

        try {
            coffee.setDate(LocalDate.parse(dto.getDate())); // yyyy-MM-dd
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("날짜 형식이 잘못되었거나 누락되었습니다. yyyy-MM-dd 형식으로 입력해주세요.");
        }

        coffee.setUserId(userId);
        coffeeRepository.save(coffee);
    }

    public Map<String, Integer> getDailyCoffeeCounts(String month, int userId) {
        List<CoffeeConsumption> records = coffeeRepository.findByUserIdAndMonth(userId, month);

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
        return coffeeRepository.findByUserIdAndMonth(userId, month)
                .stream()
                .mapToInt(CoffeeConsumption::getQuantity)
                .sum();
    }

    // 3. 가장 자주 마신 메뉴
    public String getMostFrequentCoffeeType(String month, int userId) {
        List<CoffeeConsumption> records = coffeeRepository.findByUserIdAndMonth(userId, month);
        Map<String, Integer> countMap = new HashMap<>();
        for (CoffeeConsumption r : records) {
            countMap.put(r.getCoffeeType(), countMap.getOrDefault(r.getCoffeeType(), 0) + r.getQuantity());
        }
        return countMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("데이터 없음");
    }
}
