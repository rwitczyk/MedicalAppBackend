package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class NurseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nurseId;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private UserLoginDataEntity userLoginDataEntity;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nurseEntity")
    private List<NurseTaskEntity> nurseTaskEntities;
}
