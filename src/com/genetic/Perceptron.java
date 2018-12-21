package com.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Perceptron {
    private Integer[] numberOfNeuronsInLayers;
    private Integer numberOfInputs;
    private Function<Double, Double> activationFunction;

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }

    private List<Double> weights = new ArrayList<>();

    public List<Double> getWeights() {
        return weights;
    }

    public Double getResult() {
        return result;
    }

    private List<Layer> layers = new ArrayList<>();
    private Double result;

    public Perceptron(Function<Double, Double> activationFunction, Integer numberOfInputs, Integer... numberOfNeuronsInLayers) {
        this.numberOfNeuronsInLayers = numberOfNeuronsInLayers;
        this.activationFunction = activationFunction;
        this.numberOfInputs = numberOfInputs;
        initWeight();
        initLayers();
    }

    public Double test(List<List<Double>> inputs, List<Integer> expectedOutputs) {
        List<List<Double>> outputsFromPerceptron = testPerceptron(inputs);
        Integer verifiedPositively = verifyResults(expectedOutputs, outputsFromPerceptron);
        result = (double)verifiedPositively/inputs.size();
        return result;
    }

    private Integer verifyResults(List<Integer> expectedOutputs, List<List<Double>> outputsFromPerceptron) {
        Integer verifiedPositively = 0;

        for (int outputIndex = 0; outputIndex < outputsFromPerceptron.size(); outputIndex++){
            if(verify(outputIndex, outputsFromPerceptron.get(outputIndex), expectedOutputs)){
                verifiedPositively = verifiedPositively+1;
            }
        }
        return verifiedPositively;
    }

    private List<List<Double>> testPerceptron(List<List<Double>> inputs) {
        List<List<Double>> outputs = new ArrayList<>();
        for (int inputIndex = 0; inputIndex<inputs.size(); inputIndex++){
            Integer weightIndex = 0;
            Integer neuronsInPrevLayer = inputs.get(inputIndex).size();
            List<Double> outputsFromPreviousLayer = inputs.get(inputIndex);
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = layers.get(i);
                outputsFromPreviousLayer = layer.apply(outputsFromPreviousLayer, weights.subList(weightIndex, weightIndex + (neuronsInPrevLayer*layer.getNumberOfNeurons())));
                weightIndex = weightIndex + neuronsInPrevLayer*layer.getNumberOfNeurons();
                neuronsInPrevLayer = layer.getNumberOfNeurons();
            }
            outputs.add(outputsFromPreviousLayer);
        }
        return outputs;
    }

    private boolean verify(Integer elementIndex, List<Double> output, List<Integer> expectedOutputs){
        Integer maxIndex = 0;
        for(int i=0; i<output.size(); i++){
            if (output.get(i) > output.get(maxIndex)){
                maxIndex = i;
            }
        }
        Integer expectedIndex = expectedOutputs.get(elementIndex);
        return maxIndex.equals(expectedIndex);
    }

    private void initWeight(){
        Integer numberOfWeight = findNumberOfWeight();
        Random random = new Random();
        for (int i=0; i<numberOfWeight; i++){
            weights.add(-10 + (random.nextDouble() * 20));
        }
    }

    private void initLayers(){
        for(Integer numberOfNeurons : numberOfNeuronsInLayers){
            layers.add(new Layer(numberOfNeurons, activationFunction));
        }
    }

    private Integer findNumberOfWeight(){
        Integer numberOfWeight = 0;
        Integer neuronsInPrevLayer = numberOfInputs;
        for(Integer numberOfNeurons : numberOfNeuronsInLayers){
            numberOfWeight = numberOfWeight + numberOfNeurons * neuronsInPrevLayer;
            neuronsInPrevLayer = numberOfNeurons;
        }
        return numberOfWeight;
    }
}
