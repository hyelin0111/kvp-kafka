package com.kvp.kafka.consumer;

import com.kvp.domain.Introduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KvpTestConsumer {

    @KafkaListener(topics = "kvp-output", groupId = "kvp", containerFactory = "introduceListener")
    public void consume(Introduce message) {
        log.info("consume message : {}", message);
    }
}
