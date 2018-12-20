package com.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Layer {
    private Integer numberOfNeurons;
    private Function<Double, Double> acceptanceFunction;
    private List<Neuron> neurons = new ArrayList<>();

    public Layer(Integer numberOfNeurons, Function<Double, Double> acceptanceFunction) {
        this.numberOfNeurons = numberOfNeurons;
        this.acceptanceFunction = acceptanceFunction;
        initNeurons();
    }

    public List<Double> apply(List<Double> inputs, List<Double> weights){
        Integer weightIndex = 0;
        List<Double> outputs = new ArrayList<>();
        for (int i=0; i<numberOfNeurons; i++){
            outputs.add(neurons.get(i).apply(inputs, weights.subList(weightIndex, weightIndex+inputs.size())));
            weightIndex = weightIndex + inputs.size();
        }
        return outputs;
    }

    private void initNeurons(){
        for(int i=0; i<this.numberOfNeurons; i++){
            neurons.add(new Neuron(acceptanceFunction));
        }
    }

    public Integer getNumberOfNeurons() {
        return numberOfNeurons;
    }
}
