package dmcs.rwitczyk.domains;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Data
public class NurseTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private PatientEntity patientEntity;

    @ManyToOne
    private NurseEntity nurseEntity;

    @NotBlank
    private String taskName;

    private String fromTime;

    private String toTime;

    private Date date;

    private String description;

    private String price;
}
