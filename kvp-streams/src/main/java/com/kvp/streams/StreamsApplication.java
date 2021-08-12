package com.kvp.streams;

import com.kvp.domain.AnonymousProgrammer;
import com.kvp.domain.Introduce;
import com.kvp.domain.ProgramLanguage;
import com.kvp.domain.Programmer;
import com.kvp.streams.serdes.AnonymousProgrammerSerde;
import com.kvp.streams.serdes.IntroduceSerde;
import com.kvp.streams.serdes.ProgrammerSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Objects;
import java.util.Properties;

public class StreamsApplication {
    public static void main(String[] args) {

        /** step1
         * 1. kvp-input 토픽에서 Introduce 객체를 받아, kvp-output 토픽에 저장하는 기능을 구현하세요.
         * 2. kvp-input 토픽에서 받은 Introduce객체에 다음과 같은 작업을 진행합니다.
         *   - 나이를 10살 더해주세요.
         *   - 이름의 마스킹을 진행해 주세요.
         */

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kvp-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Serde<String> stringSerde = Serdes.String();
        IntroduceSerde introduceSerde = new IntroduceSerde();

        KStream<String, Introduce> firstStream = streamsBuilder.stream("kvp-input", Consumed.with(stringSerde, introduceSerde))
        .mapValues( value -> {
            Introduce introduce = new Introduce();
            introduce.setAge(value.getAge());
            introduce.setName(value.getName());

            introduce.addAge();
            introduce.maskingName();

            return introduce;
        });

        firstStream.to("kvp-output", Produced.with(stringSerde, introduceSerde));

        /** step2
         * 1. 소스 : programmer (이름, 나이, 언어, 연차)
         * 2. 연차 기준 분리 - 3년차 이하는 주니어 싱크, 3년차 초과는 시니어 싱크
         * 3. 시니어에서 자바 개발자만 필터링해서 나이, 연차만 싱크
        */
        ProgrammerSerde programmerSerde = new ProgrammerSerde();
        AnonymousProgrammerSerde anonymousProgrammerSerde = new AnonymousProgrammerSerde();
        KStream<String, Programmer> programmerStream = streamsBuilder.stream("programmer", Consumed.with(stringSerde, programmerSerde));
        programmerStream.print(Printed.<String,Programmer>toSysOut().withLabel("programmerStream"));

        Predicate<String, Programmer> isJunior = (key, programmer) -> programmer.isJunior();
        Predicate<String, Programmer> isSenior = (key, programmer) -> programmer.isSenior();
        KStream<String, Programmer>[] programmerStreamByYear = programmerStream.branch(isJunior,isSenior);

        programmerStreamByYear[0].to("junior", Produced.with(stringSerde, programmerSerde));     // 주니어 개발자 싱크
        programmerStreamByYear[1].to("senior", Produced.with(stringSerde, programmerSerde));     // 시니어 개발자 싱크

        programmerStreamByYear[1].filter((key, programmer) ->
                Objects.equals(programmer.getLanguage(), ProgramLanguage.JAVA))
                .mapValues(programmer -> {
                    AnonymousProgrammer anonymousProgrammer = new AnonymousProgrammer();
                    anonymousProgrammer.setAge(programmer.getAge());
                    anonymousProgrammer.setYear(programmer.getYear());
                    return anonymousProgrammer;
                })
                .to("senior-java", Produced.with(stringSerde, anonymousProgrammerSerde));       // 시니어 자바 개발자 싱크

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        kafkaStreams.start();
    }
}