package bank.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "EMAIL_DATA")
@NoArgsConstructor
public class EmailData {

    @Id
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="USER_ID", referencedColumnName="ID", nullable = false)
    private User user;

    @Column(name = "EMAIL", nullable = false)
    @Size(max = 200)
    @Email
    private String email;

    public EmailData(User user, String email) {
        this.id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
        this.user = user;
        this.email = email;
    }
}
