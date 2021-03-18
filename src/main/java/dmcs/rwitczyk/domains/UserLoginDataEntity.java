package dmcs.rwitczyk.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Min(value = 6)
    private String password;

    private boolean enabled = false;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RoleEntity roleEntity;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true, mappedBy = "userLoginDataEntity")
    private PatientEntity patientEntity;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true, mappedBy = "userLoginDataEntity")
    private DoctorEntity doctorEntity;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true, mappedBy = "userLoginDataEntity")
    private NurseEntity nurseEntity;
}
