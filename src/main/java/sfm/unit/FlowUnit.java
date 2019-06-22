package sfm.unit;

import sfm.unit.exceptions.UnmountableUnitException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FlowUnit<I, O> {
    private Class<I> inputClass;
    private Class<O> outputClass;

    private FlowUnit parent;
    private FlowUnit child;

    public FlowUnit() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        inputClass = (Class<I>) actualTypeArguments[0];
        outputClass = (Class<O>) actualTypeArguments[1];
    }

    public abstract O process(I input);

    public boolean canBeMountedBefore(FlowUnit flowUnit) {
        return this.outputClass == flowUnit.inputClass;
    }

    public boolean canBeMountedAfter(FlowUnit flowUnit) {
        return flowUnit.outputClass == this.inputClass;
    }

    public FlowUnit mountToThis(FlowUnit flowUnit) throws UnmountableUnitException {
        if (canBeMountedBefore(flowUnit)) {
            this.child = flowUnit;
            return flowUnit;
        } else {
            throw new UnmountableUnitException();
        }
    }

    public FlowUnit getChild() {
        return child;
    }
}
