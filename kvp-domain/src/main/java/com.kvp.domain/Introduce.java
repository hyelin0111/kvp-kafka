package com.kvp.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Introduce {
    private String name;
    private Long age;
    private ProgramLanguage language;
    private int year;

    public void addAge() {
        age += 10;
    }

    public void maskingName() {
        if(name.length() < 3) return;

        name = name.replace(name.substring(1, name.length()-1), "*");
    }

    public boolean isJunior() {
        return year <=3 ;
    }

    public boolean isSenior() {
        return !isJunior();
    }
}
