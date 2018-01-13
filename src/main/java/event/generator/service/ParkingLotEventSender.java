package event.generator.service;

import event.generator.model.ParkingLotEvent;
import lombok.extern.java.Log;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log
public class ParkingLotEventSender {

    private final JmsTemplate jmsTemplate;

    public ParkingLotEventSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(ParkingLotEvent event) {
        log.info("Sending event to queue");

        jmsTemplate.convertAndSend("DLQ", event);
    }

}
