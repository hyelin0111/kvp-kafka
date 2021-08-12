package com.kvp.streams.serdes;

import com.kvp.domain.Introduce;
import com.kvp.domain.Programmer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class ProgrammerSerde extends Serdes.WrapperSerde<Programmer> {
    public ProgrammerSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(Programmer.class));
    }
}
