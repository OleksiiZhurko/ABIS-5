package com.gen.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RandomBag {

    private int elements;
    private int population;
    private int iterations;
    private int bag_size;
    private int range_costs;
    private int range_weights;
    private double mutation_rate;

}
