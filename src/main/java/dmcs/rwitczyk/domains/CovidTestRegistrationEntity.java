package dmcs.rwitczyk.domains;

import dmcs.rwitczyk.models.CovidTestRegistrationStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CovidTestRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private UserLoginDataEntity userLoginDataEntity;

    private Date visitDate;

    private CovidTestRegistrationStatusEnum status;

    private String fromTime;

    private String toTime;

    private String price;

    private boolean pcrTest;

    private boolean antygenTest;

    private boolean seroTest;
}
