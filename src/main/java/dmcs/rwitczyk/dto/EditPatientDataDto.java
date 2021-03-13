package dmcs.rwitczyk.dto;

import lombok.Data;

@Data
public class EditPatientDataDto {

    private int patientId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;
}
