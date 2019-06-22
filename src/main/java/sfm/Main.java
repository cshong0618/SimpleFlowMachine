package sfm;

import sfm.engine.SimpleFlow;
import sfm.engine.exceptions.UnmodifiableFlowException;
import sfm.unit.FlowUnit;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleFlow<String, String> simpleFlow = new SimpleFlow<>();

        FlowUnit<String, Double> convertToDouble = new FlowUnit<String, Double>("convertToDouble") {
            @Override
            public Double process(String input) {
                return Double.parseDouble(input);
            }
        };

        FlowUnit<Double, Double> multiplyBy2 = new FlowUnit<Double, Double>("multiplyBy2") {
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

        listFlow(simpleFlow.listFlow());

        String output = simpleFlow.run("4.3");
        System.out.println(output);

        String output2 = simpleFlow.startFrom("multiplyBy2", 9999.0);
        System.out.println(output2);

        // We can still add flow here
        FlowUnit<String, String> convertToDoubleMultiplyBy3AndConvertToString = new FlowUnit<String, String>(
                "convertToDoubleMultiplyBy3AndConvertToString"
        ) {
            @Override
            public String process(String input) {
                Double v = Double.parseDouble(input);
                v *= 3;

                return v.toString();
            }
        };

        simpleFlow.then(convertToDoubleMultiplyBy3AndConvertToString);
        listFlow(simpleFlow.listFlow());
        String output4 = simpleFlow.run("1");
        System.out.println(output4);

        // We can freeze it
        simpleFlow.done();
        try {
            simpleFlow.then(new FlowUnit<String, String>("appendSomething") {
                @Override
                public String process(String input) {
                    return input + "::NOT_RAN";
                }
            });
        } catch (UnmodifiableFlowException ufe) {
            System.out.println("We got an exception");
        }
    }

    public static void listFlow(List<String> flows) {
        flows
                .stream()
                .reduce((s, s2) -> s + " --> " + s2)
                .ifPresent(System.out::println);
    }
}
