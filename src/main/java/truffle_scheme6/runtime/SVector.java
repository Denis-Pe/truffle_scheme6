package truffle_scheme6.runtime;

import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;

public class SVector {
    private final Object[] elms;

    public SVector(Object[] elms) {
        this.elms = elms;
    }

    public Object[] getElms() {
        return elms;
    }

    @Override
    public String toString() {
        return "#(" + StringFormatting.separatedBy(" ", elms) + ")";
    }
}
