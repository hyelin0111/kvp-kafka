package com.kvp.streams.serdes;

import com.kvp.domain.GradeAccumulator;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PurchaseGradeSerde extends Serdes.WrapperSerde<GradeAccumulator> {
    public PurchaseGradeSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(GradeAccumulator.class));
    }
}
