package com.kvp.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Introduce {
    private String name;
    private int age;

    public void addAge() {
        age += 10;
    }

    public void maskingName() {
        if(name.length() < 3) return;

        name = name.replace(name.substring(1, name.length()-1), "*");
    }
}
