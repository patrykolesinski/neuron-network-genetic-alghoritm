package com.genetic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneticAlghoritmInvocator {

    public Perceptron findTheBestPerceptron(Integer sizeOfPopulation, List<List<Double>> inputs, List<Integer> expectedOutputs, Function<Double, Double> activationFunction) {

        List<Perceptron> population = initPopulation(sizeOfPopulation, activationFunction);
        testPopulation(inputs, expectedOutputs, population);
        Perceptron bestPerceptron = null;

        while (true) {
            population = findPerceptronsToNewPopulation(population);
            crossing(population);
            mutation(population);
            testPopulation(inputs, expectedOutputs, population);
            bestPerceptron = findBestPerceptron(population);
            if (bestPerceptron.getResult() > 0.90) {
                return bestPerceptron;
            }
        }
    }

    private Perceptron findBestPerceptron(List<Perceptron> population){
        return population.stream().sorted(Comparator.comparing(Perceptron::getResult).reversed()).findFirst().get();
    }

    private List<Perceptron> findPerceptronsToNewPopulation(List<Perceptron> perceptrons){
        List<Perceptron> newPopulation = new ArrayList<>();
        perceptrons = perceptrons.stream().sorted(Comparator.comparing(Perceptron::getResult)).collect(Collectors.toList());
        Random random = new Random();
        int theBestPerceptionsNumber =  Math.round(0.90f * perceptrons.size());
        int theWorstPerceptionsNumber = Math.round(0.10f * perceptrons.size());

        for (int i=0; i<theBestPerceptionsNumber; i++){
            newPopulation.add(perceptrons.get(perceptrons.size()-((1 + random.nextInt(theWorstPerceptionsNumber)))));
        }

        while(newPopulation.size()<perceptrons.size()){
            newPopulation.add(perceptrons.get(random.nextInt(perceptrons.size()-1)));
        }

        return newPopulation;
    }

    private void crossing(List<Perceptron> population){
        Random random = new Random();
        Double crossingChance = 0.80;
        population.forEach(perceptron -> {
            if(random.nextDouble() < crossingChance){
                Perceptron perceptronToCross = population.get(random.nextInt(population.size()-1));
                int crossingIndex = 2 + random.nextInt(population.get(0).getWeights().size() - 2);
                List<Double> weights1 = perceptron.getWeights();
                List<Double> weights2 = perceptronToCross.getWeights();
                List<Double> newWeights1 = new ArrayList<>();
                newWeights1.addAll(weights2.subList(0, crossingIndex));
                newWeights1.addAll(weights1.subList(crossingIndex, weights1.size()));
                List<Double> newWeights2 = new ArrayList<>();
                newWeights2.addAll(weights1.subList(0, crossingIndex));
                newWeights2.addAll(weights2.subList(crossingIndex, weights2.size()));
                perceptron.setWeights(newWeights1);
                perceptronToCross.setWeights(newWeights2);
            }
        });
    }

    private void mutation(List<Perceptron> population){
        Random random = new Random();
        Double mutationChance = 0.40;
        population.forEach(perceptron -> {
                for (int i=0; i<perceptron.getWeights().size(); i++){
                    if(random.nextDouble() < mutationChance){
                        perceptron.getWeights().set(i, -10 + (random.nextDouble() * 20));
                    }
                }
        });
    }

    private void testPopulation(List<List<Double>> inputs, List<Integer> expectedOutputs, List<Perceptron> population) {
        population.forEach(perceptron -> {
            perceptron.test(inputs, expectedOutputs);
        });
    }

    private List<Perceptron> initPopulation(Integer sizeOfPopulation, Function<Double, Double> activationFunction) {
        List<Perceptron> population = new ArrayList<>();
        for (int i=0; i<sizeOfPopulation; i++){
            population.add(new Perceptron(activationFunction, 2, 5, 5 ,2));
        }
        return population;
    }

    ;
}
