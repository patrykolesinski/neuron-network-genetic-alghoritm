package com.genetic;

import java.util.List;
import java.util.function.Function;

public class Neuron {
    private List<Double> inputs;
    private List<Double> weights;
    private Double output;
    private Function<Double, Double> function;

    public Neuron(Function function) {
        this.function = function;
    }

    public Double apply(List<Double> inputs, List<Double> weights){
        Double membranePotential = calculateMembranePotential(inputs, weights);
        return calculateActivationFunction(membranePotential);
    }

    private Double calculateMembranePotential(List<Double> inputs, List<Double> weights){
        Double sum = 0.0;
        for (int i=0; i<inputs.size(); i++){
            sum = sum + inputs.get(i) * weights.get(i);
        }
        return sum;
    }

    private Double calculateActivationFunction(Double membranePotential){
        return function.apply(membranePotential);
    }
}
