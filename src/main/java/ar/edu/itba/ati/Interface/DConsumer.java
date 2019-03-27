package ar.edu.itba.ati.Interface;

@FunctionalInterface
public interface DConsumer<R,S> {
    void accept(R r,S s);
}
