package org.example.uberprojectsocketserver.producers;

import org.example.uberprojectsocketserver.dto.UpdateBookingRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private  final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, UpdateBookingRequestDto> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String,UpdateBookingRequestDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void publishMessage(String topic, UpdateBookingRequestDto message){
        kafkaTemplate.send(topic,message);
        logger.info("Message published to Kafka topic: {} ", topic);
    }

}
