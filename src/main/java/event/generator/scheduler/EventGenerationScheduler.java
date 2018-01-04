package event.generator.scheduler;

import event.generator.model.ParkingLot;
import event.generator.model.ParkingLotEvent;
import event.generator.model.ParkingLotType;
import event.generator.service.ParkingLotService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static event.generator.model.ParkingLotEventType.FREE;
import static event.generator.model.ParkingLotEventType.OCCUPY;
import static java.util.Objects.nonNull;

@Component
@Log
public class EventGenerationScheduler {
    private final ParkingLotService parkingLotService;
    private List<Long> parkingLotIds;
    private Random random = new Random(System.currentTimeMillis());

    @Autowired
    public EventGenerationScheduler(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostConstruct
    public void init() {
        log.info("Post construct");

        List<ParkingLot> allParkingLots = parkingLotService.getAllParkingLots();

        parkingLotIds = allParkingLots.stream()
                .map(ParkingLot::getId)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("Generating event :) ");

        if (parkingLotIds.size() < 1) return;

        int parkingLotIndex = random.nextInt(parkingLotIds.size());
        ParkingLot parkingLotById = parkingLotService.getParkingLotById(parkingLotIds.get(parkingLotIndex));

        if (nonNull(parkingLotById)) {
            Set<Map.Entry<ParkingLotType, Integer>> entries = parkingLotById.getParkingCapacity().entrySet();
            int entriesNumber = entries.size();
            int entryIndex = random.nextInt(entriesNumber);
            Map.Entry<ParkingLotType, Integer> entry = null;

            for (Map.Entry<ParkingLotType, Integer> e : entries) {
                if (entryIndex-- == 0) {
                    entry = e;
                    break;
                }
            }

            int spotsAvailable = entry.getValue();
            int spotsOccupied = parkingLotById.getParkingSpotsOccupied().get(entry.getKey());

            ParkingLotEvent.ParkingLotEventBuilder builder = ParkingLotEvent.builder();

            if (spotsOccupied == spotsAvailable || (random.nextInt(100) < 50 && spotsOccupied>1)) {
                builder.eventType(OCCUPY);
            } else {
                builder.eventType(FREE);
            }

            ParkingLotEvent event = builder
                    .parkingLotId(parkingLotById.getId())
                    .parkingLotType(entry.getKey())
                    .spots(1)
                    .build();

            log.info(event.toString());

            sendToQueue(event);
        } else {
            log.warning("Parking lot with id " + parkingLotIds.get(parkingLotIndex) + " has not been found");
        }
    }

    private void sendToQueue(ParkingLotEvent event) {
        //TODO implement sending to queue here
        log.info("Sending event to queue");
    }

}
