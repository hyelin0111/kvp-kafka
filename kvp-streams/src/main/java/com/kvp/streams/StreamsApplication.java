package com.kvp.streams;

import com.kvp.domain.*;
import com.kvp.domain.GradeAccumulator;
import com.kvp.domain.Purchase;
import com.kvp.domain.step4.Commute;
import com.kvp.domain.step4.CommuteByEmp;
import com.kvp.domain.step4.CommuteType;
import com.kvp.streams.serdes.*;
import com.kvp.streams.serdes.step4.CommuteByEmpSerde;
import com.kvp.streams.serdes.step4.CommuteSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.time.Duration;
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

        KStream<String, AnonymousProgrammer> isseniorJava =  programmerStreamByYear[1].filter((key, programmer) ->
                        Objects.equals(programmer.getLanguage(), ProgramLanguage.JAVA))
                //selectKey를 하고 mapValues, transformValues, flatMapValues 사용 시 자동으로 리파티셔닝
                .selectKey((k,v) -> v.getName())
                .mapValues(programmer -> {
                    AnonymousProgrammer anonymousProgrammer = new AnonymousProgrammer();
                    anonymousProgrammer.setAge(programmer.getAge());
                    anonymousProgrammer.setYear(programmer.getYear());
                    return anonymousProgrammer;
                });
        isseniorJava.print(Printed.<String, AnonymousProgrammer>toSysOut().withLabel("seniorJavaStream"));
        isseniorJava.to("senior-java", Produced.with(stringSerde, anonymousProgrammerSerde));       // 시니어 자바 개발자 싱크

        /** step3
         * 1. 소스 : purchase
         * 2. 구매 금액 합계 별 등급
         *   - 브론즈 : 10만원 이하
         *   - 실버 : 10만원 초과 ~ 20만원 이하
         *   - 골드 : 20만원 초과 ~ 50만원 이하
         *   - 플래티넘 : 50만원 초과 ~ 100만원 이하
         *   - VIP : 100만원 초과
         */
        PurchaseSerde purchaseSerde = new PurchaseSerde();
        PurchaseGradeSerde purchaseGradeSerde = new PurchaseGradeSerde();
        KStream<String, Purchase> purchaseKStream = streamsBuilder.stream("purchase", Consumed.with(stringSerde, purchaseSerde));
        purchaseKStream.print(Printed.<String, Purchase>toSysOut().withLabel("purchaseStream"));

        String gradeStateStoreName = "gradeStateStore";
        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore(gradeStateStoreName); // StateStore 공급자 생성

        StoreBuilder<KeyValueStore<String, Long>> storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, Serdes.String(), Serdes.Long());
//                .withLoggingDisabled(); // 상태 저장소에 대한 변경로그를 저장하지 않기 위해 사용(사용 안하면 계속 누적)

        streamsBuilder.addStateStore(storeBuilder); // 상태 저장소를 토폴로지에 추가

        KStream<String, GradeAccumulator> statefulGradeAccumulator =
                purchaseKStream.transformValues(() -> new PurchaseGradeTransformer(gradeStateStoreName), gradeStateStoreName);
        statefulGradeAccumulator.to("grade", Produced.with(stringSerde, purchaseGradeSerde));

        /** step4
         * 1. 소스 : commute (사원번호, 이름, 출근/퇴근, 날짜+시간)
         * 2. 출근 / 퇴근 기록
         * 3. 근무 기록 (출근, 퇴근 기록 조인)
         * 4. 초과 근무 기록 (초과 근무자 필터링)
         */
        CommuteSerde commuteSerde = new CommuteSerde();
        CommuteByEmpSerde commuteByEmpSerde = new CommuteByEmpSerde();

        KStream<String, Commute> commuteKStream = streamsBuilder.stream("commute", Consumed.with(stringSerde, commuteSerde));
        commuteKStream.print(Printed.<String, Commute>toSysOut().withLabel("commuteStream"));

        Predicate<String, Commute> goToWork = (key, commute) -> Objects.equals(commute.getCommuteType(), CommuteType.출근);
        Predicate<String, Commute> getOffWork = (key, commute) -> Objects.equals(commute.getCommuteType(), CommuteType.퇴근);

        KStream<String, Commute>[] branchesStream = commuteKStream.selectKey((key, value) -> value.getNo()).branch(goToWork, getOffWork);
        KStream<String, Commute> startStream = branchesStream[0];
        KStream<String, Commute> endStream = branchesStream[1];

        ValueJoiner<Commute, Commute, CommuteByEmp> commuteJoiner = new CommuteJoiner();
        JoinWindows twentyFourHoursWindow = JoinWindows.of(Duration.ofHours(24));

        KStream<String, CommuteByEmp> joinedKStream = startStream.join(endStream,
                commuteJoiner,
                twentyFourHoursWindow,
                StreamJoined.with(stringSerde, commuteSerde, commuteSerde));

        joinedKStream.print(Printed.<String, CommuteByEmp>toSysOut().withLabel("joined CommuteStream"));

        joinedKStream.to("commuteByEmp", Produced.with(stringSerde, commuteByEmpSerde));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        kafkaStreams.start();
    }
}