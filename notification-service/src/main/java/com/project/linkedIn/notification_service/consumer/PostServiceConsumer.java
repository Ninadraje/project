package com.project.linkedIn.notification_service.consumer;

import com.project.linkedIn.event.PostCreatedEvent;
import com.project.linkedIn.event.PostLikedEvent;
import com.project.linkedIn.notification_service.SendNotification;
import com.project.linkedIn.notification_service.clients.ConnectionsClient;
import com.project.linkedIn.notification_service.dto.PersonDto;
import com.project.linkedIn.notification_service.entity.Notification;
import com.project.linkedIn.notification_service.respository.NotificationRepository;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;

    private final NotificationRepository notificationRepository;

    private final SendNotification sendNotification;

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PostServiceConsumer.class);

    public PostServiceConsumer(ConnectionsClient connectionsClient, NotificationRepository notificationRepository, SendNotification sendNotification) {
        this.connectionsClient = connectionsClient;
        this.notificationRepository = notificationRepository;
        this.sendNotification = sendNotification;
    }

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Handle post-created-topic: {}", postCreatedEvent.toString());

        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for (PersonDto connection : connections) {
            sendNotification.send(connection.getUserId(),
                    "Your Connection" +
                            postCreatedEvent.getCreatorId() +
                            "has created a post");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Handle post-liked-topic: {}", postLikedEvent.toString());

        sendNotification.send(postLikedEvent.getCreatorId(),
                "Your Connection" +
                        postLikedEvent.getLikedByUserId() +
                        "has Liked your post" + postLikedEvent.getPostId());

    }


    public ConnectionsClient getConnectionsClient() {
        return this.connectionsClient;
    }

    public NotificationRepository getNotificationRepository() {
        return this.notificationRepository;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PostServiceConsumer)) return false;
        final PostServiceConsumer other = (PostServiceConsumer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$connectionsClient = this.getConnectionsClient();
        final Object other$connectionsClient = other.getConnectionsClient();
        if (this$connectionsClient == null ? other$connectionsClient != null : !this$connectionsClient.equals(other$connectionsClient))
            return false;
        final Object this$notificationRepository = this.getNotificationRepository();
        final Object other$notificationRepository = other.getNotificationRepository();
        if (this$notificationRepository == null ? other$notificationRepository != null : !this$notificationRepository.equals(other$notificationRepository))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PostServiceConsumer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $connectionsClient = this.getConnectionsClient();
        result = result * PRIME + ($connectionsClient == null ? 43 : $connectionsClient.hashCode());
        final Object $notificationRepository = this.getNotificationRepository();
        result = result * PRIME + ($notificationRepository == null ? 43 : $notificationRepository.hashCode());
        return result;
    }

    public String toString() {
        return "PostServiceConsumer(connectionsClient=" + this.getConnectionsClient() + ", notificationRepository=" + this.getNotificationRepository() + ")";
    }
}
