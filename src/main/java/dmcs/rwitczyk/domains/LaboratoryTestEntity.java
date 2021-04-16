package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LaboratoryTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String description;

    @JsonManagedReference
    @ManyToOne
    private PatientEntity patientEntity;

    @JsonManagedReference
    @ManyToOne
    private NurseEntity nurseEntity;

    private boolean pcrTest;

    private boolean antygenTest;

    private boolean seroTest;

    private boolean covidResult;
}
