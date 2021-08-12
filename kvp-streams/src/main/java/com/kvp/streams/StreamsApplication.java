package com.kvp.streams;

import com.kvp.domain.AnonymousIntroduce;
import com.kvp.domain.Introduce;
import com.kvp.domain.ProgramLanguage;
import com.kvp.streams.serdes.AnonymousIntroduceSerde;
import com.kvp.streams.serdes.IntroduceSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.internals.InternalTopicConfig;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

//https://coding-start.tistory.com/138
public class StreamsApplication {
    public static void main(String[] args) {
        //TODO 이곳에 KafkaStreams 실습을 작성해 주시면 됩니다!
        /** step1
         * 1. kvp-input 토픽에서 Introduce 객체를 받아, kvp-output 토픽에 저장하는 기능을 구현하세요.
         * 2. kvp-input 토픽에서 받은 Introduce객체에 다음과 같은 작업을 진행합니다.
         *   - 나이를 10살 더해주세요.
         *   - 이름의 마스킹을 진행해 주세요.
         *
         * step2
         * 1. 소스 : kvp-input (이름, 나이, 언어, 연차(1이상))
         * 2. 연차 기준 분리 - 3년차 이하는 주니어 싱크, 3년차 초과는 시니어 싱크
         * 3. 시니어 싱크 중 자바 개발자만 필터링
         */

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kvp-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        StreamsConfig streamsConfig = new StreamsConfig(props);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Serde<String> stringSerde = Serdes.String();
        IntroduceSerde introduceSerde = new IntroduceSerde();
        AnonymousIntroduceSerde anonymousIntroduceSerde = new AnonymousIntroduceSerde();

        KStream<String, Introduce> firstStream = streamsBuilder.stream("kvp-input", Consumed.with(stringSerde, introduceSerde))
        .mapValues( value -> {
            Introduce introduce = new Introduce();
            introduce.setAge(value.getAge());
            introduce.setName(value.getName());
            introduce.setLanguage(value.getLanguage());
            introduce.setYear(value.getYear());

            introduce.addAge();
            introduce.maskingName();

            return introduce;
        });

        firstStream.print(Printed.<String,Introduce>toSysOut().withLabel("firstStream"));

//        firstStream.to("kvp-output", Produced.with(stringSerde, introduceSerde));

        KStream<String, Introduce>[] kStreamByYear = firstStream.branch(
                (key, introduce) -> introduce.isJunior(),
                (key, introduce) -> introduce.isSenior()
        );

        kStreamByYear[0].to("junior", Produced.with(stringSerde, introduceSerde));
        kStreamByYear[1].to("senior", Produced.with(stringSerde, introduceSerde));

        KStream<String, AnonymousIntroduce> kStreamSeniorJava =
        kStreamByYear[1].filter((key, introduce) ->
                Objects.equals(introduce.getLanguage(), ProgramLanguage.JAVA))
                .mapValues(introduce -> {
                            AnonymousIntroduce anonymousIntroduce = new AnonymousIntroduce();
                            anonymousIntroduce.setAge(introduce.getAge());
                            anonymousIntroduce.setYear(introduce.getYear());
                            return anonymousIntroduce;
                        });
        kStreamSeniorJava.to("senior-java", Produced.with(stringSerde, anonymousIntroduceSerde));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),props);
        kafkaStreams.start();
    }
}