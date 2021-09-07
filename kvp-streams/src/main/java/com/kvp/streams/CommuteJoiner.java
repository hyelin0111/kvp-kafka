package com.kvp.streams;

import com.kvp.domain.step4.Commute;
import com.kvp.domain.step4.CommuteByEmp;
import org.apache.kafka.streams.kstream.ValueJoiner;

import java.time.temporal.ChronoUnit;

public class CommuteJoiner implements ValueJoiner<Commute, Commute, CommuteByEmp> {

    @Override
    public CommuteByEmp apply(Commute commute1, Commute commute2) {
        CommuteByEmp commuteByEmp = new CommuteByEmp();
        Long diff = ChronoUnit.HOURS.between(commute1.getDateTime(), commute2.getDateTime());

        commuteByEmp.setNo(commute1.getNo());
        commuteByEmp.setName(commute1.getName());
        commuteByEmp.setLocalDate(commute1.getDateTime().toLocalDate());
        commuteByEmp.setBusinessHours(diff);

        return commuteByEmp;
    }
}
