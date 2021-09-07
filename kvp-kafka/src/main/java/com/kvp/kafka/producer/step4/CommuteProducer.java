package com.kvp.kafka.producer.step4;

import com.kvp.domain.step4.Commute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class CommuteProducer {
    private static final String TOPIC = "commute";
    private final KafkaTemplate<String, Commute> kafkaTemplate;

    public CommuteProducer(KafkaTemplate<String, Commute> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Commute commute) {
        log.info("produce Commute message : {}", commute);
        kafkaTemplate.send(TOPIC, commute);
    }
}
