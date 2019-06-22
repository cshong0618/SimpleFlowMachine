package sfm.engine;

import java.util.concurrent.Callable;

public interface IFlow {
    public void setErrorHandler(FlowErrorHandler<Exception> errorHandler);
}
