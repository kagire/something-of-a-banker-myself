package bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
}
