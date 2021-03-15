package dmcs.rwitczyk.dto;

import dmcs.rwitczyk.models.VisitStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableVisitsListDto {

    private int id;

    private VisitStatusEnum status;

    private String fromTime;

    private String toTime;

    private String date;

    private String description;

    private String price;
}
