package com.gen.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BagDecision {

    private int bag_size;
    private int[] costs;
    private int[] weights;
    private int[] result;
    private int fitness;
    private int max_fitness;
    private int iterations;
    private long elapsed_time;

}
