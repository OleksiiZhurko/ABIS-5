package com.gen.model;

import com.gen.dto.GivenBag;
import com.gen.dto.RandomBag;
import com.gen.dto.BagDecision;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BagSolver {

    private final int DECISION = 0;

    private int SUM_COST;
    private int ELEMENTS;
    private int BAG_SIZE;
    private int POPULATION;
    private int ITERATIONS;
    private int NUMBER_RANGE;
    private double MUTATION_RATE;
    private int BEST_COST;
    private int[] COSTS;
    private int[] WEIGHTS;

    public BagDecision solveBag(GivenBag givenBag) {
        this.COSTS = givenBag.getCosts();
        this.WEIGHTS = givenBag.getWeights();

        this.BEST_COST = givenBag.getBestCost();
        this.SUM_COST = BEST_COST;
        this.ELEMENTS = COSTS.length;
        this.BAG_SIZE = givenBag.getBag_size();
        this.POPULATION = givenBag.getPopulation();
        this.ITERATIONS = 100_000;
        this.NUMBER_RANGE = (int) Math.pow(2, ELEMENTS);
        this.MUTATION_RATE = givenBag.getMutation_rate();

        return solveBag();
    }

    public BagDecision solveBag(RandomBag randomBag) {
        this.COSTS = generateArr(randomBag.getRange_costs(), randomBag.getElements());
        this.WEIGHTS = generateArr(randomBag.getRange_weights(), randomBag.getElements());

        this.SUM_COST = Arrays.stream(COSTS).sum();
        this.ELEMENTS = randomBag.getElements();
        this.BAG_SIZE = randomBag.getBag_size();
        this.POPULATION = randomBag.getPopulation();
        this.ITERATIONS = randomBag.getIterations();
        this.NUMBER_RANGE = (int) Math.pow(2, ELEMENTS);
        this.MUTATION_RATE = randomBag.getMutation_rate();
        this.BEST_COST = 0;

        return solveBag();
    }

    private BagDecision solveBag() {
        System.out.println(BAG_SIZE);
        long time = System.currentTimeMillis();

        List<Chromosome> population = new ArrayList<>(POPULATION);

        for (int one = 0; one < POPULATION; one++) {
            population.add(
                    Chromosome.builder()
                            .chromosome(generateBitsFromInt((int) (Math.random() * NUMBER_RANGE), ELEMENTS))
                            .build()
            );
        }

        updateFitness(population);
        sortPopulationByFitness(population);
        printPopulation(population);

        int index = -1;

        while (++index < ITERATIONS && population.get(0).getFitness() > DECISION) {
            population = produceNextGeneration(population);
            //printPopulation(population);
        }

        printPopulation(population);

        System.out.println("========================================================\n");

        return BagDecision.builder()
                .costs(COSTS)
                .weights(WEIGHTS)
                .bag_size(BAG_SIZE)
                .result(population.get(0).getChromosome())
                .fitness(population.get(0).getFitness())
                .max_fitness(SUM_COST)
                .iterations(index)
                .elapsed_time(System.currentTimeMillis() - time)
                .build();
    }

    private int[] generateArr(int generationRange, int elements) {
        int[] res = new int[elements];

        for (int one = 0; one < elements; one++) {
            res[one] = (int) (Math.random() * generationRange) + 1;
        }

        return res;
    }

    private int getBit(int num, int index) {
        return (int) ((num >> index) & 1);
    }

    private int[] generateBitsFromInt(int value, int size) {
        int[] res = new int[size];

        for (int one = size - 1, two = 0; one >= 0; one--, two++) {
            res[two] = getBit(value, one);
        }

        return res;
    }

    private void updateFitness(List<Chromosome> chromosomes) {
        chromosomes.forEach(elem -> elem.setFitness(fitnessCost(elem.getChromosome())));
    }

    private void sortPopulationByFitness(List<Chromosome> population) {
        population.sort(Comparator.comparingInt(Chromosome::getFitness));
    }

    private void printPopulation(List<Chromosome> population) {
        population.forEach(System.out::println);
        System.out.println();
    }

    private List<Chromosome> produceNextGeneration(List<Chromosome> list) {
        List<Chromosome> helpList = new ArrayList<>(ELEMENTS);
        int[] b;

        for (int one = 0; helpList.size() < POPULATION; one++) {
            int point = (int) (Math.random() * (ELEMENTS - 1));

            b = crossOver(
                    list.get(one).getChromosome(),
                    list.get(one + 1).getChromosome(),
                    point
            );

            helpList.add(
                    Chromosome.builder()
                            .chromosome(b)
                            .build()
            );

            b = crossOver(
                    list.get(one + 1).getChromosome(),
                    list.get(one).getChromosome(),
                    point
            );

            helpList.add(
                    Chromosome.builder()
                            .chromosome(b)
                            .build()
            );
        }

        mutate(helpList);
        updateFitness(helpList);

        return mergeArrays(list, helpList);
    }

    private List<Chromosome> mergeArrays(List<Chromosome> population1, List<Chromosome> population2) {
        population2.addAll(population1);
        population2.sort(Comparator.comparingInt(Chromosome::getFitness));

        return population2.stream().limit(population1.size()).collect(Collectors.toList());
    }

    private void mutate(List<Chromosome> children) {
        children.forEach(elem -> {
            if (Math.random() < MUTATION_RATE) {
                elem.setChromosome(generateBitsFromInt((int) (Math.random() * NUMBER_RANGE), ELEMENTS));
            }
        });
    }

    private int fitnessCost(int[] chromosome) {
        int sumCosts = 0;
        int sumWeights = 0;

        for (int one = 0; one < chromosome.length; one++) {
            if (chromosome[one] == 1) {
                sumCosts   += COSTS[one];
                sumWeights += WEIGHTS[one];
            }
        }

        if (sumWeights > BAG_SIZE) {
            return SUM_COST + 1;
        }

        return SUM_COST - sumCosts;
    }

    private int[] crossOver(int[] chromosome1, int[] chromosome2, int point) {
        if (Arrays.equals(chromosome1, chromosome2)) {
            return chromosome1;
        }

        int[] res = new int[ELEMENTS];

        if (point + 1 >= 0) {
            System.arraycopy(chromosome1, 0, res, 0, point + 1);
        }

        if (ELEMENTS - point + 1 >= 0) {
            System.arraycopy(chromosome2, point + 1, res, point + 1, ELEMENTS - point - 1);
        }

        return res;
    }

}
