package com.gen.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GivenBag {

    private int bestCost;
    private int population;
    private int bag_size;
    private double mutation_rate;
    private int[] costs;
    private int[] weights;

}
