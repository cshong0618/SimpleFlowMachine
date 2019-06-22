package sfm;

import sfm.engine.SimpleFlow;
import sfm.unit.FlowUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleFlow<String, String> simpleFlow = new SimpleFlow<>();

        FlowUnit<String, Double> convertToDouble = new FlowUnit<String, Double>() {
            @Override
            public Double process(String input) {
                return Double.parseDouble(input);
            }
        };

        FlowUnit<Double, Double> multiplyBy2 = new FlowUnit<Double, Double>() {
            @Override
            public Double process(Double input) {
                return input * 2;
            }
        };

        FlowUnit<Double, String> convertToString = new FlowUnit<Double, String>() {
            @Override
            public String process(Double input) {
                return input.toString();
            }
        };

        simpleFlow.start(convertToDouble)
                .then(multiplyBy2)
                .then(convertToString);

        String output = simpleFlow.run("4.3");
        System.out.println(output);
    }
}
