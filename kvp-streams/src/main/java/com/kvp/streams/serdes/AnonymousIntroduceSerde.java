package com.kvp.streams.serdes;

import com.kvp.domain.AnonymousIntroduce;
import com.kvp.domain.Introduce;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class AnonymousIntroduceSerde extends Serdes.WrapperSerde<AnonymousIntroduce> {
    public AnonymousIntroduceSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(AnonymousIntroduce.class));
    }
}
