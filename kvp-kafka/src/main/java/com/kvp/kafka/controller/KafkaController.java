package com.kvp.kafka.controller;

import com.kvp.domain.Introduce;
import com.kvp.kafka.producer.KvpTestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KvpTestProducer kvpTestProducer;

    public KafkaController(KvpTestProducer kvpTestProducer) {
        this.kvpTestProducer = kvpTestProducer;
    }

    @GetMapping
    public ResponseEntity send(String name, Long age) {
        Introduce introduce = new Introduce(name, age);
        kvpTestProducer.send(introduce);
        return ResponseEntity.ok().build();
    }
}
