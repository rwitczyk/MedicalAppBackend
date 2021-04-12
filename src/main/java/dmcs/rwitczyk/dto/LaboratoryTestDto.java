package dmcs.rwitczyk.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryTestDto {

    private int patientId;
    private int nurseId;
    private String subject;
    private String description;
    private boolean pcr;
    private boolean antygen;
    private boolean sero;
}
