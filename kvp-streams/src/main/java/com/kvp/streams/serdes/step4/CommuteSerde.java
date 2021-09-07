package com.kvp.streams.serdes.step4;

import com.kvp.domain.step4.Commute;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CommuteSerde extends Serdes.WrapperSerde<Commute> {
    public CommuteSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(Commute.class));
    }
}
