package bank.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SearchRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
}
