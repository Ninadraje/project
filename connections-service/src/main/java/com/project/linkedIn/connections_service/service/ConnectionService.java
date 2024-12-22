package com.project.linkedIn.connections_service.service;


import com.project.linkedIn.connections_service.entity.Person;
import com.project.linkedIn.connections_service.repository.PersonRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class ConnectionService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ConnectionService.class);
    private final PersonRepository personRepository;

    public ConnectionService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getFirstDegreeConenctions(Long userId) {
        log.info("Getting first degree connections for user with id: {}", userId);

        return personRepository.getFirstDegreeConnection(userId);
    }
}
