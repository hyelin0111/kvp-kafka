package com.kvp.streams;

import com.kvp.domain.Introduce;
import com.kvp.streams.serdes.IntroduceSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import java.util.Properties;

//https://coding-start.tistory.com/138
public class StreamsApplication {
    public static void main(String[] args) {
        //TODO 이곳에 KafkaStreams 실습을 작성해 주시면 됩니다!
        /**
         * 1. kvp-input 토픽에서 Introduce 객체를 받아, kvp-output 토픽에 저장하는 기능을 구현하세요.
         * 2. kvp-input 토픽에서 받은 Introduce객체에 다음과 같은 작업을 진행합니다.
         *   - 나이를 10살 더해주세요.
         *   - 이름의 마스킹을 진행해 주세요.
         */

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kvp-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        StreamsConfig streamsConfig = new StreamsConfig(props);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Serde<String> stringSerde = Serdes.String();
        IntroduceSerde introduceSerde = new IntroduceSerde();

        KStream<String, Introduce> firstStream = streamsBuilder.stream("kvp-input", Consumed.with(stringSerde, introduceSerde));

        firstStream.mapValues(
                value->{
                    value.addAge();
                    return value;
                }
        );

        firstStream.to("kvp-output", Produced.with(stringSerde, introduceSerde));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),props);
        kafkaStreams.start();
    }
}