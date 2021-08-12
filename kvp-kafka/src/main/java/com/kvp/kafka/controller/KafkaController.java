package com.kvp.kafka.controller;

import com.kvp.domain.Introduce;
import com.kvp.domain.ProgramLanguage;
import com.kvp.domain.Programmer;
import com.kvp.kafka.producer.IntroduceProducer;
import com.kvp.kafka.producer.ProgrammerProducer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final IntroduceProducer introduceProducer;
    private final ProgrammerProducer programmerProducer;

    public KafkaController(IntroduceProducer introduceProducer, ProgrammerProducer programmerProducer) {
        this.introduceProducer = introduceProducer;
        this.programmerProducer = programmerProducer;
    }

    @GetMapping
    public ResponseEntity send(String name, Long age) {
        Introduce introduce = new Introduce(name, age);
        introduceProducer.send(introduce);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/programmer")
    public ResponseEntity send(String name, Long age, ProgramLanguage language, int year) {
        Programmer programmer = new Programmer(name, age, language, year);
        programmerProducer.send(programmer);
        return ResponseEntity.ok().build();
    }
}
