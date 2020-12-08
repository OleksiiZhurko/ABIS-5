package com.gen.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Chromosome {

    private int    fitness;
    private int[] chromosome;

}
