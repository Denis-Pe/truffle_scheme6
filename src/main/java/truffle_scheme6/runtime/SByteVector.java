package truffle_scheme6.runtime;

import truffle_scheme6.utils.StringFormatting;

public class SByteVector {
    private final byte[] value;

    public SByteVector(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "#vu8(" + StringFormatting.spaced(value) + ")";
    }
}
