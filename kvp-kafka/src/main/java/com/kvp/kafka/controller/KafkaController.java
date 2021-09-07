package com.kvp.kafka.controller;

import com.kvp.domain.*;
import com.kvp.domain.step4.Commute;
import com.kvp.domain.step4.CommuteType;
import com.kvp.kafka.producer.step4.CommuteProducer;
import com.kvp.kafka.producer.IntroduceProducer;
import com.kvp.kafka.producer.ProgrammerProducer;
import com.kvp.kafka.producer.PurchaseProducer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final IntroduceProducer introduceProducer;
    private final ProgrammerProducer programmerProducer;
    private final PurchaseProducer purchaseProducer;
    private final CommuteProducer commuteProducer;

    public KafkaController(IntroduceProducer introduceProducer, ProgrammerProducer programmerProducer, PurchaseProducer purchaseProducer, CommuteProducer commuteProducer) {
        this.introduceProducer = introduceProducer;
        this.programmerProducer = programmerProducer;
        this.purchaseProducer = purchaseProducer;
        this.commuteProducer = commuteProducer;
    }

    @GetMapping
    public ResponseEntity send(String name, int age) {
        Introduce introduce = new Introduce(name, age);
        introduceProducer.send(introduce);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/programmer")
    public ResponseEntity send(String name, int age, ProgramLanguage language, int year) {
        Programmer programmer = new Programmer(name, age, language, year);
        programmerProducer.send(programmer);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/purchase")
    public ResponseEntity send(String name, Long price) {
        Purchase purchase = new Purchase(name, price);
        purchaseProducer.send(purchase);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/commute")
    public ResponseEntity send(String no, String name, CommuteType commuteType, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime dateTime) {
        Commute commute = new Commute(no, name, commuteType, dateTime);
        commuteProducer.send(commute);
        return ResponseEntity.ok().build();
    }
}
