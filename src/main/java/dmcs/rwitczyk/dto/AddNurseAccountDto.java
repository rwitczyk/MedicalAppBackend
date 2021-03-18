package dmcs.rwitczyk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNurseAccountDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
