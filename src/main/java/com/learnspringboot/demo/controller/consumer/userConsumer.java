package com.learnspringboot.demo.controller.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnspringboot.demo.config.KafkaConfigConstant;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.db.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

//@Component
public class userConsumer {
    private static final Logger logger = LoggerFactory.getLogger(userConsumer.class);

    @Autowired
    private UserService userService;

    @KafkaListener(groupId = KafkaConfigConstant.GROUP_ID_JSON, topics = KafkaConfigConstant.TOPIC_CREATE,  containerFactory = KafkaConfigConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageCreate(Object message) throws JsonProcessingException {
        ConsumerRecord x = (ConsumerRecord)message;
        User user = (User)x.value();
        userService.save(user);
    }

    @KafkaListener(groupId = KafkaConfigConstant.GROUP_ID_JSON, topics = KafkaConfigConstant.TOPIC_UPDATE,  containerFactory = KafkaConfigConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageUpdate(Object message) throws JsonProcessingException {
        ConsumerRecord x = (ConsumerRecord)message;
        User user = (User)x.value();
        userService.update(user);
    }

    @KafkaListener(groupId = KafkaConfigConstant.GROUP_ID_JSON, topics = KafkaConfigConstant.TOPIC_DELETE,  containerFactory = KafkaConfigConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageDelete(Object message) throws JsonProcessingException {
        ConsumerRecord x = (ConsumerRecord)message;
        Long id = (Long)x.value();
        userService.deleteById(id);
    }
}
