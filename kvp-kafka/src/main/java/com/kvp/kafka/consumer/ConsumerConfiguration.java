package com.kvp.kafka.consumer;

import com.kvp.domain.*;
import com.kvp.domain.GradeAccumulator;
import com.kvp.domain.step4.CommuteByEmp;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

    public static Map<String, Object> consumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "kvp");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return configs;
    }

    @Bean
    public ConsumerFactory<String, Introduce> introduceConsumerConfigs() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(Introduce.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Introduce> introduceListener() {
        ConcurrentKafkaListenerContainerFactory<String, Introduce> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(introduceConsumerConfigs());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Programmer> programmerConsumerConfigs() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(Programmer.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Programmer> programmerListener() {
        ConcurrentKafkaListenerContainerFactory<String, Programmer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(programmerConsumerConfigs());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AnonymousProgrammer> anonymousProgrammerConsumerConfigs() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(AnonymousProgrammer.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AnonymousProgrammer> anonymousProgrammerListener() {
        ConcurrentKafkaListenerContainerFactory<String, AnonymousProgrammer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(anonymousProgrammerConsumerConfigs());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, GradeAccumulator> gradeConsumerConfigs() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(GradeAccumulator.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GradeAccumulator> gradeListener() {
        ConcurrentKafkaListenerContainerFactory<String, GradeAccumulator> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(gradeConsumerConfigs());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CommuteByEmp> commuteConsumerConfigs() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(CommuteByEmp.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CommuteByEmp> commuteListener() {
        ConcurrentKafkaListenerContainerFactory<String, CommuteByEmp> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commuteConsumerConfigs());
        return factory;
    }
}
