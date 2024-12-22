package com.project.linkedIn.connections_service.controller;

import com.project.linkedIn.connections_service.entity.Person;
import com.project.linkedIn.connections_service.service.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionsController(ConnectionService connectionService) {
        this.connectionService = connectionService; // Injected via constructor
    }

    @GetMapping("/{userId}/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(@PathVariable Long userId){
        return  ResponseEntity.ok(connectionService.getFirstDegreeConenctions(userId));
    }



}
