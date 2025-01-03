package com.project.linkedIn.connections_service.controller;

import com.project.linkedIn.connections_service.entity.Person;
import com.project.linkedIn.connections_service.service.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionService connectionService;


    //Using Constructor Injector, because Lombok annotation is not working at the time
    @Autowired
    public ConnectionsController(ConnectionService connectionService) {
        this.connectionService = connectionService; // Injected via constructor
    }

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(){
        return  ResponseEntity.ok(connectionService.getFirstDegreeConenctions());
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.rejectConnectionRequest(userId));
    }



}
