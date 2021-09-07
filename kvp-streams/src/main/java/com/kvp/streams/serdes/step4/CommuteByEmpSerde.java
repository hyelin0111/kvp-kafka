package com.kvp.streams.serdes.step4;

import com.kvp.domain.step4.CommuteByEmp;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CommuteByEmpSerde extends Serdes.WrapperSerde<CommuteByEmp> {
    public CommuteByEmpSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(CommuteByEmp.class));
    }
}
