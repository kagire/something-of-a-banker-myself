package bank.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "PHONE_DATA")
@NoArgsConstructor
public class PhoneData {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID", referencedColumnName="ID", nullable = false)
    private User user;

    @Column(name = "PHONE", nullable = false)
    @Size(min = 6, max = 13)
    private String phone;

    public PhoneData(User user, String phone) {
        this.id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
        this.user = user;
        this.phone = phone;
    }
}
