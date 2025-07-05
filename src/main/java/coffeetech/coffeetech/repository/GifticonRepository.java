package coffeetech.coffeetech.repository;

import coffeetech.coffeetech.entity.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
    Optional<Gifticon> findFirstByUserIsNull(); // 아직 지급되지 않은 기프티콘 1개
    List<Gifticon> findByUser_UserId(Long userId); // 유저의 기프티콘 목록
}
