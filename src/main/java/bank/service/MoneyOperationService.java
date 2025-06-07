package bank.service;

import bank.dao.AccountDepositRepository;
import bank.dao.AccountRepository;
import bank.dao.UserRepository;
import bank.model.dto.UserDTO;
import bank.model.entity.Account;
import bank.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MoneyOperationService {

    private static final BigDecimal increaseDecimal = new BigDecimal("0.1");
    private static final BigDecimal maxDecimal = new BigDecimal("2.07");
    private static final Duration incrementInterval = Duration.ofSeconds(30);

    private final AccountRepository accountRepository;
    private final AccountDepositRepository accountDepositRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public UserDTO transfer(BigDecimal amount, User fromUser, long toUserId) {

        Account fromAccount = accountRepository.findByUserForUpdate(fromUser)
            .orElseThrow(() -> new IllegalStateException("account absent!"));
        if (fromAccount.getBalance().compareTo(amount) < 0)
            throw new IllegalStateException("insufficient balance!");

        User toUser = userRepository
            .findById(toUserId)
            .orElseThrow(() -> new IllegalArgumentException("designated user is absent!"));

        Account toAccount = accountRepository.findByUserForUpdate(toUser)
            .orElseThrow(() -> new IllegalStateException("designated account absent!"));;

        try {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } catch (Exception e) {
            throw new IllegalStateException("cannot do money transfer!");
        }

        return userService.getUserDTO(fromUser.getId());
    }

    @Transactional
    @Scheduled(fixedRate = 10_000)
    public void incrementDeposit() {
        LocalDateTime now = LocalDateTime.now();

        var depositAccounts = accountDepositRepository.findAll();
        depositAccounts.forEach(da -> {
            if (da.getInitialBalance().compareTo(BigDecimal.ZERO) == 0) {
                accountDepositRepository.delete(da);
                return;
            }

            var interval = Duration.between(da.getLastIncrementAt(), now);
            var incrementCycles = interval.dividedBy(incrementInterval);

            if (incrementCycles < 1) return;
            BigDecimal currBalance = da.getCurrentBalance().setScale(2, RoundingMode.HALF_DOWN);
            Account account = accountRepository.getReferenceById(da.getAccountId());

            for (var i = 0; i < incrementCycles; i++) {
                BigDecimal addition = currBalance.multiply(increaseDecimal);

                if (currBalance.add(addition).compareTo(da.getInitialBalance().multiply(maxDecimal)) > 0) {
                    accountDepositRepository.delete(da);
                } else {
                    da.setLastIncrementAt(now);
                    da.setCurrentBalance(currBalance.add(addition));
                    account.setBalance(account.getBalance().add(addition).setScale(2, RoundingMode.HALF_DOWN));
                    accountRepository.save(account);
                    accountDepositRepository.save(da);
                }
            }
        });
    }
}
