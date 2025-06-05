package bank.dao;

import bank.model.entity.EmailData;
import bank.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    EmailData findByEmail(String email);
    List<EmailData> findAllByUser(User user);
}
