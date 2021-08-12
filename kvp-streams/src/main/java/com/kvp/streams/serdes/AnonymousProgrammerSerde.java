package com.kvp.streams.serdes;

import com.kvp.domain.AnonymousProgrammer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class AnonymousProgrammerSerde extends Serdes.WrapperSerde<AnonymousProgrammer> {
    public AnonymousProgrammerSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(AnonymousProgrammer.class));
    }
}
