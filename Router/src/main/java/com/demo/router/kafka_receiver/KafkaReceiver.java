package com.demo.router.kafka_receiver;

import java.io.StringReader;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.demo.router.DTO.TransactionRequest;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

@Service
public class KafkaReceiver {
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "payments.incoming", groupId = "payment-router")
    public void receiveFromKafka(ConsumerRecord<String, String> record) {
        String xmlMessage = record.value();
        try {
            // Convert XML string to TransactionRequest object
            TransactionRequest request = convertXmlToObject(xmlMessage);
            String paymentType = request.getTransaction().getPaymentType();

            String targetTopic = switch (paymentType.toLowerCase()) {
                case "inward" -> "payments.inward";
                case "outward" -> "payments.outward";
                default -> {
                    System.err.println("Unknown payment type: " + paymentType);
                    yield null;
                }
            };

            if (targetTopic != null) {
                kafkaTemplate.send(targetTopic, xmlMessage);
                System.out.println("Message routed to topic: " + targetTopic);
            }

        } catch (Exception e) {
            System.err.println("Failed to process message:");
            e.printStackTrace();
        }
    }

    private TransactionRequest convertXmlToObject(String xml) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(TransactionRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (TransactionRequest) unmarshaller.unmarshal(new StringReader(xml));
    }
}
