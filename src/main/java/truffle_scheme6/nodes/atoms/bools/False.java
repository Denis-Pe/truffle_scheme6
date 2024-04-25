package truffle_scheme6.nodes.atoms.bools;

public final class False extends Bool {
    public final static False S = new False();

    private False() {
    }

    @Override
    public String toString() {
        return "#F";
    }
}
