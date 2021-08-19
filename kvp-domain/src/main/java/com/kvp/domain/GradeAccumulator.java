package com.kvp.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GradeAccumulator {
    private String name;
    private Long price;
    private Long totalPrice;
    private Grade grade;
}
