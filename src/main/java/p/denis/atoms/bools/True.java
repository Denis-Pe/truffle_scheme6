package p.denis.atoms.bools;

public final class True extends Bool {
    public final static True S = new True();

    private True() {
    }

    @Override
    public String toString() {
        return "#T";
    }
}
