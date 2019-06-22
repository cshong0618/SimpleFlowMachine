package sfm.engine;

public interface FlowErrorHandler<T> {
    public void run(T ex);
}
