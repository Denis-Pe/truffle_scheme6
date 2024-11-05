package truffle_scheme6.runtime.numbers;

public interface SComplex<R, I> extends SNumber {
    R getReal();

    I getImag();
}
