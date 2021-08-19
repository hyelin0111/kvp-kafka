package com.kvp.streams.serdes;

import com.kvp.domain.Purchase;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PurchaseSerde extends Serdes.WrapperSerde<Purchase> {
    public PurchaseSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(Purchase.class));
    }
}
