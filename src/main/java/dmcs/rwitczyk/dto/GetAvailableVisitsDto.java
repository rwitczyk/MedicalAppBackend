package dmcs.rwitczyk.dto;

import lombok.Data;

@Data
public class GetAvailableVisitsDto {

    private int doctorId;

    private String date;
}
