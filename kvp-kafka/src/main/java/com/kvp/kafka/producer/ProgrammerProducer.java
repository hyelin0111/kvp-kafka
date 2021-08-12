package com.kvp.kafka.producer;

import com.kvp.domain.Introduce;
import com.kvp.domain.Programmer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class ProgrammerProducer {
    private static final String TOPIC = "programmer";
    private final KafkaTemplate<String, Programmer> kafkaTemplate;

    public ProgrammerProducer(KafkaTemplate<String, Programmer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Programmer programmer) {
        log.info("produceProgrammer message : {}", programmer);
        kafkaTemplate.send(TOPIC, programmer);
    }
}
