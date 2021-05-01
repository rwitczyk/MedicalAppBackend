package dmcs.rwitczyk.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AvailableVisitsForCovidTestDto {

    private Long id;

    private Date visitDate;

    private String fromTime;

    private String toTime;
}
