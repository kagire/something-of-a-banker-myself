package bank.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "ID")
    private long id;

    @OneToOne
    @JoinColumn(name="USER_ID", referencedColumnName="ID", nullable = false, unique = true)
    private User user;

    @Column(name = "BALANCE", nullable = false)
    @DecimalMin(value = "0")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private BigDecimal balance;

    public Account(User user, BigDecimal balance) {
        this.id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
        this.user = user;
        this.balance = balance;
    }
}
