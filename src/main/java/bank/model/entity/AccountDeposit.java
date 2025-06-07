package bank.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ACCOUNT_DEPOSIT_INCREMENT")
@NoArgsConstructor
public class AccountDeposit {

    @Id
    @Column(name = "ACCOUNT_ID")
    private long accountId;

    @Column(name = "INITIAL_BALANCE")
    private BigDecimal initialBalance;

    @Column(name = "CURRENT_BALANCE")
    private BigDecimal currentBalance;

    @Column(name = "LAST_INCREMENT_AT", nullable = false)
    private LocalDateTime lastIncrementAt;
}
