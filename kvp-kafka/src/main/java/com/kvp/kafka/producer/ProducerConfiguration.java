package com.kvp.kafka.producer;

import com.kvp.domain.Introduce;
import com.kvp.domain.Programmer;
import com.kvp.domain.Purchase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

//https://docs.spring.io/spring-kafka/docs/current/reference/html/#kafka-template
@Configuration
public class ProducerConfiguration {
    @Bean
    public ProducerFactory<String, Introduce> introduceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, Programmer> programmerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, Purchase> purchaseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Introduce> introduceKafkaTemplate() {
        return new KafkaTemplate<>(introduceProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, Programmer> programmerKafkaTemplate() {
        return new KafkaTemplate<>(programmerProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, Purchase> purchaseKafkaTemplate() {
        return new KafkaTemplate<>(purchaseProducerFactory());
    }

}
