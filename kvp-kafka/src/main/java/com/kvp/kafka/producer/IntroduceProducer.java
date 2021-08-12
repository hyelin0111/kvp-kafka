package com.kvp.kafka.producer;

import com.kvp.domain.Introduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IntroduceProducer {
    private static final String TOPIC = "kvp-input";
    private final KafkaTemplate<String, Introduce> kafkaTemplate;

    public IntroduceProducer(KafkaTemplate<String, Introduce> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Introduce introduce) {
        log.info("produceIntroduce message : {}", introduce);
        kafkaTemplate.send(TOPIC, introduce);
    }
}
