package com.kvp.streams;

import com.kvp.domain.Grade;
import com.kvp.domain.GradeAccumulator;
import com.kvp.domain.Purchase;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Optional;

public class PurchaseGradeTransformer implements ValueTransformer<Purchase, GradeAccumulator> {
    private KeyValueStore<String, Long> stateStore;
    private final String storeName;
    private ProcessorContext context;

    public PurchaseGradeTransformer(String storeName) {
        this.storeName = storeName;
    }

    public void init(ProcessorContext context) {
        this.context = context;
        stateStore = this.context.getStateStore(storeName);
    }

    @Override
    public GradeAccumulator transform(Purchase value) {

        Long beforeTotalPrice = Optional.ofNullable(stateStore.get(value.getName())).orElse(0L);
        Long totalPurchasePrice = beforeTotalPrice + value.getPrice();

        stateStore.put(value.getName(), totalPurchasePrice);
        GradeAccumulator rewardAccumulator = new GradeAccumulator(value.getName(), value.getPrice(), totalPurchasePrice, Grade.getRank(totalPurchasePrice));

        return rewardAccumulator;
    }

    @Override
    public void close() {

    }

}
