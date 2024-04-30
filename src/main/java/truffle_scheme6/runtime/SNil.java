package truffle_scheme6.runtime;

public class SNil {
    public static final SNil SINGLETON = new SNil();

    private SNil() {}

    @Override
    public String toString() {
        return "()";
    }
}
