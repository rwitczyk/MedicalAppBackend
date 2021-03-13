package dmcs.rwitczyk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AcceptVisitDto {

    private int visitId;

    private String description;

    private String price;
}
