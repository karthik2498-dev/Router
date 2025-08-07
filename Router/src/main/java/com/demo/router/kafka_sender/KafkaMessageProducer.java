package com.demo.router.kafka_sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate,
                                @Value("${app.kafka.topic.input}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public String sendMessage(String xmlPayload) {
        kafkaTemplate.send(topic, xmlPayload);
        System.out.println("Sent XML to topic: " + topic);
        return "Sent XML to topic: " + topic;
    }
}
