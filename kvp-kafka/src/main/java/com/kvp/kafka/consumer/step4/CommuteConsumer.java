package com.kvp.kafka.consumer.step4;

import com.kvp.domain.step4.CommuteByEmp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommuteConsumer {

    @KafkaListener(topics = "commuteByEmp", groupId = "kvp", containerFactory = "commuteListener")
    public void consumeGrade(CommuteByEmp message) {
        log.info("commute message : {}", message);
    }

}
