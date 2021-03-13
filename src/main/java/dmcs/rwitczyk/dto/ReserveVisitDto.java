package dmcs.rwitczyk.dto;

import lombok.Data;

@Data
public class ReserveVisitDto {

    private int doctorId;

    private String visitDate;

    private int visitId;

    private int patientAccountId;
}
