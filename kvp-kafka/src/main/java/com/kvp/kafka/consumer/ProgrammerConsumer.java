package com.kvp.kafka.consumer;

import com.kvp.domain.AnonymousProgrammer;
import com.kvp.domain.Introduce;
import com.kvp.domain.Programmer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProgrammerConsumer {

    @KafkaListener(topics = "junior", groupId = "kvp", containerFactory = "programmerListener")
    public void consumeJunior(Programmer message) {
        log.info("consumeJunior message : {}", message);
    }

    @KafkaListener(topics = "senior", groupId = "kvp", containerFactory = "programmerListener")
    public void consumeSenior(Programmer message) {
        log.info("consumeSenior message : {}", message);
    }

    @KafkaListener(topics = "senior-java", groupId = "kvp", containerFactory = "anonymousProgrammerListener")
    public void consumeSeniorJava(AnonymousProgrammer message) { log.info("consumeSeniorJava message : {}", message); }

}
