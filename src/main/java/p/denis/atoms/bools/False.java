package p.denis.atoms.bools;

public final class False extends Bool {
    public final static False S = new False();

    private False() {
    }

    @Override
    public String toString() {
        return "#F";
    }
}
