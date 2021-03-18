package dmcs.rwitczyk.domains;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Data
public class NurseTaskEntity {

    @Id
    private Long id;

    @OneToOne
    private PatientEntity patientEntity;

    @OneToOne
    private NurseEntity nurseEntity;

    @NotBlank
    private String taskName;

    private String fromTime;

    private String toTime;

    private Date date;

    private String description;

    private String price;
}
