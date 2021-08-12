package com.kvp.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Programmer {
    private String name;
    private Long age;
    private ProgramLanguage language;
    private int year;

    public boolean isJunior() { return year <=3; }

    public boolean isSenior() { return !isJunior(); }
}
