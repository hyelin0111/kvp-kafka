package com.kvp.kafka.producer;

import com.kvp.domain.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class PurchaseProducer {
    private static final String TOPIC = "purchase";
    private final KafkaTemplate<String, Purchase> kafkaTemplate;

    public PurchaseProducer(KafkaTemplate<String, Purchase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Purchase purchase) {
        log.info("produce Purchase message : {}", purchase);
        kafkaTemplate.send(TOPIC, purchase);
    }
}
