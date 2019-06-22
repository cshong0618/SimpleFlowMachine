package sfm.engine;

import sfm.engine.exceptions.NoRootException;
import sfm.engine.exceptions.NonUniqueFlowUnitException;
import sfm.unit.FlowUnit;
import sfm.unit.exceptions.UnmountableUnitException;

public abstract class Flow<I, O> implements IFlow{
    protected Flow() {}
    public abstract Flow start(FlowUnit flowUnit);
    public abstract Flow then(FlowUnit flowUnit) throws NoRootException, UnmountableUnitException, NonUniqueFlowUnitException;
    public abstract O run(I input) throws Exception;
}
