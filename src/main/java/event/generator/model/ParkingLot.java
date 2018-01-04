package event.generator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ParkingLot {
    private long id;
    private GeoPoint location;
    private String address;
    private String pricing;
    private String security;
    private String restrictions;
    private String additionalInformation;
    private String googleStreetViewLink;
    private Map<ParkingLotType, Integer> parkingCapacity;
    private Map<ParkingLotType, Integer> parkingSpotsOccupied;
}
