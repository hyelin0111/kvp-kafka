package com.kvp.kafka.consumer;

import com.kvp.domain.AnonymousIntroduce;
import com.kvp.domain.Introduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KvpTestConsumer {

    @KafkaListener(topics = {"kvp-output","junior","senior"}, groupId = "kvp", containerFactory = "introduceListener")
    public void consume(Introduce message) {
        log.info("consume message : {}", message);
    }

    @KafkaListener(topics = "senior-java", groupId = "kvp", containerFactory = "anonymousIntroduceListener")
    public void consume(AnonymousIntroduce message) { log.info("consume message : {}", message); }

}
