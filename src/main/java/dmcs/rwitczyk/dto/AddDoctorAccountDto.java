package dmcs.rwitczyk.dto;

import lombok.Data;

@Data
public class AddDoctorAccountDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private String specialization;
}
