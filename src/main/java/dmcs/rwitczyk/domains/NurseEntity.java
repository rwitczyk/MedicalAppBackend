package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "nurses")
public class NurseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private UserLoginDataEntity userLoginDataEntity;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nurseEntity")
    private List<NurseTaskEntity> nurseTaskEntities;


    @Fetch(value = FetchMode.SUBSELECT)
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "nurseEntity")
    private List<LaboratoryTestEntity> laboratoryTestEntities;
}
