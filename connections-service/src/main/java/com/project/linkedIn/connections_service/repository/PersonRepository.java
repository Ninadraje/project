package com.project.linkedIn.connections_service.repository;

import com.project.linkedIn.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person,Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnection(Long userId);


    @Query("MATCH (a:Person)-[r:REQUESTED_TO]->(b:Person)" +
            "WHERE a.userId = $senderId AND b.userId = $receiverId" +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (a:Person)-[r:CONNECTED_TO]-(b:Person)" +
            "WHERE a.userId = $senderId AND b.userId = $receiverId" +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (a:Person), (b:Person)" +
            "WHERE a.userId = $senderId AND b.userId = $receiverId"+
            "CREATE (a)-[:REQUESTED_TO]->(b)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (a:Person)-[r:REQUESTED_TO]->(b:Person)" +
            "WHERE a.userId = $senderId AND b.userId = $receiverId" +
            "DELETE r"+
            "CREATE (a)-[:CONNECTED_TO]->(b)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (a:Person)-[r:REQUESTED_TO]->(b:Person)" +
            "WHERE a.userId = $senderId AND b.userId = $receiverId" +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);
}
