package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dmcs.rwitczyk.models.VisitStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OneVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @OneToOne
    private DoctorEntity doctorEntity;

    @JsonBackReference
    @OneToOne
    private PatientEntity patientEntity;

    private VisitStatusEnum status;

    private String fromTime;

    private String toTime;

    private Date date;

    private String description;

    private String price;
}
