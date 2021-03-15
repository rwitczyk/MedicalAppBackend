package dmcs.rwitczyk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptVisitDto {

    private int visitId;

    private String description;

    private String price;
}
