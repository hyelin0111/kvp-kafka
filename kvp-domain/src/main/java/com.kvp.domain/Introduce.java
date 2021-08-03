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

    public void addAge() {
        age += 10;
    }
}
