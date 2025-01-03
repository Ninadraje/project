package com.project.linkedIn.post_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * For creating Kafka topics
 * */

@Configuration
public class KafkaTopicConfig {


    @Bean
    public NewTopic postCreatedTopic(){
        return new NewTopic("post-created-topic",3,(short) 1);
    }

    @Bean
    public NewTopic postLikeTopic(){
        return new NewTopic("post-liked-topic",3,(short) 1);
    }

}
