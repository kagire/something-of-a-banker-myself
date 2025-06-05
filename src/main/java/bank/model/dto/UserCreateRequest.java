package bank.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class UserCreateRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String password;
    private String phone;
    private String email;
    private BigDecimal initialDeposit;
}
