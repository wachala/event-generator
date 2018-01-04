package event.generator.service;

import event.generator.model.ParkingLot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "parking-lot-service", fallback = ParkingLotService.ParkingLotServiceFallback.class)
@Service
public interface ParkingLotService {

    @RequestMapping(method = GET, value = "/parking-lot-service/api/parking-lot")
    List<ParkingLot> getAllParkingLots();

    @RequestMapping(method = GET, value = "/parking-lot-service/api/parking-lot/{id}")
    ParkingLot getParkingLotById(@PathVariable("id") Long id);

    @Component
    @Slf4j
    class ParkingLotServiceFallback implements ParkingLotService {

        @Override
        public List<ParkingLot> getAllParkingLots() {
            log.error("Can not load parking lots. Could not connect to parking-lot-service");

            return Collections.emptyList();
        }

        @Override
        public ParkingLot getParkingLotById(Long id) {
            log.error("Can not load parking lot with id {}. Could not connect to parking-lot-service", id);

            return null;
        }
    }
}
