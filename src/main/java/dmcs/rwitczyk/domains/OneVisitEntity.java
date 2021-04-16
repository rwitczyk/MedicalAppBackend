package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dmcs.rwitczyk.models.VisitStatusEnum;
import dmcs.rwitczyk.models.VisitTypeEnum;
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
    @ManyToOne
    private DoctorEntity doctorEntity;

    @JsonBackReference
    @ManyToOne
    private PatientEntity patientEntity;

    private VisitStatusEnum status;

    private String fromTime;

    private String toTime;

    private Date date;

    private String description;

    private String price;

    @Enumerated(EnumType.STRING)
    private VisitTypeEnum visitType;
}
