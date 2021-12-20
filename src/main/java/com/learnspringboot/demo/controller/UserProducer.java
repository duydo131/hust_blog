package com.learnspringboot.demo.controller;

import com.learnspringboot.demo.config.KafkaConfigConstant;
import com.learnspringboot.demo.config.KafkaConfiguartion;
import com.learnspringboot.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@RestController
//@RequestMapping("producer/users")
public class UserProducer {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @PostMapping()
    public String sendMessageCreate(@Validated @RequestBody User user) {
        try {
            kafkaTemplate.send(KafkaConfigConstant.TOPIC_CREATE, user);
            return " message sent succuessfully";
        } catch (Exception e) {
            e.printStackTrace();
            return " message sent fail";
        }
    }

    @PatchMapping("/{id}")
    public String sendMessageUpdate(@PathVariable("id") UUID id, @RequestBody User user) {
//        user.setId(id);
        try {
            kafkaTemplate.send(KafkaConfigConstant.TOPIC_UPDATE, user);
            return " message sent succuessfully";
        } catch (Exception e) {
            e.printStackTrace();
            return " message sent fail";
        }
    }

    @DeleteMapping("/{id}")
    public String sendMessageDelete(@PathVariable("id") Long id) {

        try {
            kafkaTemplate.send(KafkaConfigConstant.TOPIC_DELETE, id);
            return " message sent succuessfully";
        } catch (Exception e) {
            e.printStackTrace();
            return " message sent fail";
        }
    }

}
