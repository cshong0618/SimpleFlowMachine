package sfm.engine;

import sfm.engine.exceptions.NoRootException;
import sfm.engine.exceptions.NonUniqueFlowUnitException;
import sfm.unit.FlowUnit;
import sfm.unit.exceptions.UnmountableUnitException;

import java.util.HashMap;
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
        flowUnitMap.put(flowUnit.getClass().getName(), flowUnit);
        return this;
    }

    public Flow then(FlowUnit flowUnit) throws NoRootException, UnmountableUnitException, NonUniqueFlowUnitException {
        if (last == null) throw new NoRootException();
        if (flowUnitMap.containsKey(flowUnit.getClass().getName())) throw new NonUniqueFlowUnitException();
        last.mountToThis(flowUnit);
        flowUnitMap.put(flowUnit.getClass().getName(), flowUnit);
        last = last.getChild();
        return this;
    }

    @Override
    public O run(I input) throws Exception {
        if (root == null) throw new NoRootException();

        O output = null;

        try {
            FlowUnit current = root;
            Object currentVal = input;
            while (current != null) {
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


    @Override
    public void setErrorHandler(FlowErrorHandler<Exception> errorHandler) {
        this.errorHandler = errorHandler;
    }
}
