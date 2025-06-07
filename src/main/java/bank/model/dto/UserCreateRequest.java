package bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String password;
    private String phone;
    private String email;
    private BigDecimal initialDeposit;
}
