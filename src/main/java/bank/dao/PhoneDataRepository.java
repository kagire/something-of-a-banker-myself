package bank.dao;

import bank.model.entity.PhoneData;
import bank.model.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    @Cacheable(value = "phones", key = "#p0")
    PhoneData findByPhone(String phone);
    List<PhoneData> findAllByUser(User user);
}
