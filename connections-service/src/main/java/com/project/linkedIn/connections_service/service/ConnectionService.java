package com.project.linkedIn.connections_service.service;


import com.project.linkedIn.connections_service.auth.UserContextHolder;
import com.project.linkedIn.connections_service.entity.Person;
import com.project.linkedIn.connections_service.repository.PersonRepository;
import com.project.linkedIn.event.AcceptConnectionRequestEvent;
import com.project.linkedIn.event.SendConnectionRequestEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class ConnectionService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ConnectionService.class);
    private final PersonRepository personRepository;

    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    public ConnectionService(PersonRepository personRepository, KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate, KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate) {
        this.personRepository = personRepository;
        this.sendConnectionRequestEventKafkaTemplate = sendConnectionRequestEventKafkaTemplate;
        this.acceptConnectionRequestEventKafkaTemplate = acceptConnectionRequestEventKafkaTemplate;
    }

    public List<Person> getFirstDegreeConenctions() {

        Long userId=UserContextHolder.getCurrentUserId();

        log.info("Getting first degree connections for user with id: {}", userId);

        return personRepository.getFirstDegreeConnection(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {

        Long senderId= UserContextHolder.getCurrentUserId();

        log.info("Trying to send a connecetion Request, sender: {}, reciever: {}", senderId, receiverId);


        if (senderId.equals(receiverId)){
            throw  new RuntimeException("Sender and reciver are same");
        }

        boolean alreadySentRequest= personRepository.connectionRequestExists(senderId,receiverId);

        if (alreadySentRequest){
            throw new RuntimeException("Connection Request Already exists");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId,receiverId);


        if (alreadyConnected){
            throw new RuntimeException("Already connected");
        }

        log.info("Successfully sent the connecetion Request");
        personRepository.addConnectionRequest(senderId,receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent= SendConnectionRequestEvent
                .newBuilder()
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .build();

        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);

        return true;

    }

    public Boolean acceptConnectionRequest(Long senderId) {

        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists= personRepository.connectionRequestExists(senderId,receiverId);

        if (!connectionRequestExists){
            throw new RuntimeException("No Connection Request exists");
        }

        personRepository.acceptConnectionRequest(senderId,receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent= AcceptConnectionRequestEvent
                .newBuilder()
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .build();

        acceptConnectionRequestEventKafkaTemplate.send("accept-connection-request-topic",acceptConnectionRequestEvent);

        return true;

    }

    public Boolean rejectConnectionRequest(Long senderId) {

        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists= personRepository.connectionRequestExists(senderId,receiverId);

        if (!connectionRequestExists){
            throw new RuntimeException("No Connection Request exists");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);

        return true;

    }
}
