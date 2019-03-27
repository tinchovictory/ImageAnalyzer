package ar.edu.itba.ati.Interface;

@FunctionalInterface
public interface DFunction<R, S, T> {
    public T apply(R one, S two);
}
