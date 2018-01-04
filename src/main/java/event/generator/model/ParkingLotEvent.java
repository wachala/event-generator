package event.generator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingLotEvent {
    private ParkingLotEventType eventType;
    private Long parkingLotId;
    private ParkingLotType parkingLotType;
    private Integer spots;
}
