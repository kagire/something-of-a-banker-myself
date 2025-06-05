package bank.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 8, max = 500)
    private String password;

    public User(String name, LocalDate dateOfBirth, String password) {
        this.id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }
}
