package sfm.engine;

import sfm.engine.exceptions.NoRootException;
import sfm.engine.exceptions.NonUniqueFlowUnitException;
import sfm.unit.FlowUnit;
import sfm.unit.exceptions.UnmountableUnitException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleFlow<I, O> extends Flow<I, O> {
    private FlowUnit root;
    private FlowUnit last;
    private Map<String, FlowUnit> flowUnitMap;
    private FlowErrorHandler<Exception> errorHandler;

    public SimpleFlow() {
        flowUnitMap = new HashMap<>();
    }

    public SimpleFlow(FlowUnit root) {
        this();
        this.root = root;
    }

    public Flow start(FlowUnit flowUnit) {
        this.root = flowUnit;
        this.last = this.root;
        flowUnitMap.put(flowUnit.getName(), flowUnit);
        System.out.println("Added [" + flowUnit.getName() + "]");
        return this;
    }

    public Flow then(FlowUnit flowUnit) throws NoRootException, UnmountableUnitException, NonUniqueFlowUnitException {
        if (last == null) throw new NoRootException();
        if (flowUnitMap.containsKey(flowUnit.getName())) throw new NonUniqueFlowUnitException();
        last.mountToThis(flowUnit);
        flowUnitMap.put(flowUnit.getName(), flowUnit);
        last = last.getChild();
        System.out.println("Added [" + flowUnit.getName() + "] after [" + flowUnit.getParent().getName() + "]");
        return this;
    }

    @Override
    public O run(I input) throws Exception {
        return startFrom(root.getName(), input);
    }

    @Override
    public O startFrom(String flowUnitName, Object input) throws Exception {
        FlowUnit current = flowUnitMap.getOrDefault(flowUnitName, null);
        if (current == null) throw new NoRootException();

        O output = null;

        try {
            Object currentVal = input;
            while (current != null) {
                System.out.println("Running [" + current.getName() + "]");
                currentVal = current.process(currentVal);
                current = current.getChild();
            }

            output = (O)currentVal;

        } catch (Exception ex) {
            if (errorHandler != null) errorHandler.run(ex);
            else throw ex;
        }

        return output;
    }

    public List<String> listFlow() throws NoRootException {
        if (root == null) throw new NoRootException();
        List<String> flows = new LinkedList<>();
        FlowUnit current = root;

        while(current != null) {
            flows.add(current.getName());
            current = current.getChild();
        }

        return flows;
    }


    @Override
    public void setErrorHandler(FlowErrorHandler<Exception> errorHandler) {
        this.errorHandler = errorHandler;
    }
}
