package com.genetic;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Double, Double> activationFunction = (membranePotential)-> {
                return 1.0/(1+Math.pow(Math.E, -3*membranePotential));
        };
        List<List<Double>> inputs = Arrays.asList(
                Arrays.asList(0.0,0.0),
                Arrays.asList(1.0,0.0),
                Arrays.asList(0.0,1.0),
                Arrays.asList(1.0,1.0)
        );
        List<Integer> expectedOutputs = Arrays.asList(0,1,1,0);
        Perceptron perceptron = new Perceptron(activationFunction, 2, 3, 2);
        Double result = perceptron.test(inputs, expectedOutputs);
        System.out.println(result);
    }


}