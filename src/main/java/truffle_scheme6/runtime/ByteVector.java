package truffle_scheme6.runtime;

import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;

public class ByteVector {
    private final byte[] value;

    public ByteVector(byte[] value) {
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
