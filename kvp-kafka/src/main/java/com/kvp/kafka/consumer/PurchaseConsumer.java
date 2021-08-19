package com.kvp.kafka.consumer;

import com.kvp.domain.GradeAccumulator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PurchaseConsumer {

    @KafkaListener(topics = "grade", groupId = "kvp", containerFactory = "gradeListener")
    public void consumeGrade(GradeAccumulator message) {
        log.info("grade message : {}", message);
    }

}
