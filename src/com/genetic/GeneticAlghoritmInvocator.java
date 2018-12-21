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
            testPopulation(inputs, expectedOutputs, population);
            crossing2(population);
            mutation(population);
            testPopulation(inputs, expectedOutputs, population);
            bestPerceptron = findBestPerceptron(population);
            System.out.println(bestPerceptron.getResult());
            if (bestPerceptron.getResult() > 0.70) {
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

    private List<Perceptron> findPerceptronsToNewPopulation2(List<Perceptron> perceptrons){
        List<Perceptron> newPopulation = new ArrayList<>();
        perceptrons = perceptrons.stream().sorted(Comparator.comparing(Perceptron::getResult)).collect(Collectors.toList());
        Random random = new Random();
        int theBestPerceptionsNumber =  Math.round(0.60f * perceptrons.size());
        int theWorstPerceptionsNumber = Math.round(0.40f * perceptrons.size());

        for (int i=0; i<theBestPerceptionsNumber; i++){
            newPopulation.add(perceptrons.get(perceptrons.size()-((1 + random.nextInt(theWorstPerceptionsNumber)))));
        }

        while(newPopulation.size()<perceptrons.size()){
            newPopulation.add(perceptrons.get(random.nextInt(perceptrons.size()-1)));
        }

        return newPopulation;
    }

    private void crossing2(List<Perceptron> population){
        Random random = new Random();
        Double crossingChance = 1.0;

        for (int i=0; i<population.size(); i++){
            if(random.nextDouble() < crossingChance){
                Perceptron perceptron1 = population.get(i);
                int indexToCross = random.nextInt(population.size()-1);
                Perceptron perceptronToCross = population.get(indexToCross);
                List<Double> weights1 = perceptron1.getWeights();
                List<Double> weights2 = perceptronToCross.getWeights();
                if(perceptron1.getResult() > perceptronToCross.getResult()){
                    for (int j = 0 ; j<weights1.size(); j++){
                        weights2.set(j, (weights1.get(j) + weights2.get(j)) / 2);
                    }
                } else {
                    for (int j = 0 ; j<weights1.size(); j++){
                        weights1.set(j, (weights1.get(j) + weights2.get(j)) / 2);
                    }
                }
                perceptron1.setWeights(weights1);
                perceptronToCross.setWeights(weights2);
                population.set(i, perceptron1);
                population.set(indexToCross, perceptronToCross);
            }
        }
    }

    private void crossing(List<Perceptron> population){
        Random random = new Random();
        Double crossingChance = 0.8;
        population.forEach(perceptron -> {
            if(random.nextDouble() < crossingChance){
//                System.out.println("PARENT 1 " + perceptron.getWeights());
                Perceptron perceptronToCross = population.get(random.nextInt(population.size()-1));
                int crossingIndex = 2 + random.nextInt(population.get(0).getWeights().size() - 2);
//                System.out.println("PARENT 2   " +  perceptronToCross.getWeights());
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
//                System.out.println("INDEX : " + crossingIndex);
//                System.out.println("CHILD 1   " +  perceptron.getWeights());
//                System.out.println("CHILD 2   " +  perceptronToCross.getWeights());
//                System.out.println();
            }
        });
    }

    private void mutation(List<Perceptron> population){
        Double mutationChance = 0.001;
        population.forEach(perceptron -> {
            for (int i= 0; i<perceptron.getWeights().size(); i++){
                if(new Random().nextDouble() < mutationChance){
                    List<Double> weights = new ArrayList<>();
//                    int index = random.nextInt(perceptron.getWeights().size()-1);
                    for (int z = 0 ; z<population.size(); z++){
                        weights.add(population.get(z).getWeights().get(i));
                    }
//                    System.out.println(weights);
                    double gauss = new Random().nextGaussian();
                    double suma = weights.stream().mapToDouble(Double::doubleValue).sum();
                    double srednia = suma / weights.size();
                    double odchylenie =Math.sqrt(getVariance(srednia, weights));
                    Double randDouble = odchylenie * gauss + srednia;
//                    System.out.println(i);
//                    System.out.println("MUTACJA Z :" + perceptron.getWeights());
                    List<Double> weights1 = perceptron.getWeights();
                    weights1.set(i, randDouble);
                    perceptron.setWeights(weights1);
//                    System.out.println("MUTACJA!");
//                    System.out.println("NA :" + perceptron.getWeights());
                }
            }
//            perceptron.getWeights().forEach(weight -> {
//                if(random.nextDouble() < mutationChance){
////                    int index = random.nextInt(perceptron.getWeights().size()-1);
//                    Double randDouble =  -1 + (random.nextDouble() * 2);
//                    weight = weight+randDouble;
//                }
//            });

//                for (int i=0; i<perceptron.getWeights().size(); i++){
//                    if(random.nextDouble() < mutationChance){
//                        perceptron.getWeights().set(i, -10 + (random.nextDouble() * 20));
//                    }
//                }
        });
    }

    double getVariance(double srednia, List<Double> data) {
        double mean = srednia;
        double temp = 0;
        for(double a :data)
            temp += (a-mean)*(a-mean);
        return temp/(data.size());
    }

    private void testPopulation(List<List<Double>> inputs, List<Integer> expectedOutputs, List<Perceptron> population) {
        population.forEach(perceptron -> {
            perceptron.test(inputs, expectedOutputs);
        });
    }

    private List<Perceptron> initPopulation(Integer sizeOfPopulation, Function<Double, Double> activationFunction) {
        List<Perceptron> population = new ArrayList<>();
        for (int i=0; i<sizeOfPopulation; i++){
            population.add(new Perceptron(activationFunction, 16, 10 ,7));
        }
        return population;
    }

    ;
}
