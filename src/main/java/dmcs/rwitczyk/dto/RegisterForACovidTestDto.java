package dmcs.rwitczyk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForACovidTestDto {

    private Integer accountId;

    private Long visitId;

    private String visitType;

    private String price;

    private boolean pcrTest;

    private boolean antygenTest;

    private boolean seroTest;
}
