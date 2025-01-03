package com.project.linkedIn.notification_service.consumer;


import com.project.linkedIn.event.AcceptConnectionRequestEvent;
import com.project.linkedIn.event.SendConnectionRequestEvent;
import com.project.linkedIn.notification_service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private SendNotification sendNotification;


    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent){

        String message =
                "You have a received a connection from user with id: %d"+sendConnectionRequestEvent.getSenderId();


        sendNotification.send(sendConnectionRequestEvent.getReceiverId(),
                message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent){

        String message =
                "your connection request is accepted by the  user with id: %d"+acceptConnectionRequestEvent.getReceiverId();


        sendNotification.send(acceptConnectionRequestEvent.getSenderId(),
                message);
    }


}
