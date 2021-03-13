package dmcs.rwitczyk.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private UUID token;

    @OneToOne
    private UserLoginDataEntity userLoginDataEntity;

    public VerificationToken(UUID token, UserLoginDataEntity patientEntity) {
        this.token = token;
        this.userLoginDataEntity = patientEntity;
    }
}
