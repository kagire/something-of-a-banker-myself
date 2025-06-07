package bank.dao;

import bank.model.entity.AccountDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDepositRepository extends JpaRepository<AccountDeposit, Long> {
}
