package truffle_scheme6.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

import java.util.stream.IntStream;

public class SString {
    private final TruffleString value;
    private final TruffleString.ToJavaStringNode converter;

    public SString(TruffleString value) {
        this.value = value;
        this.converter = TruffleString.ToJavaStringNode.create();
    }

    public SString(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
        this.converter = TruffleString.ToJavaStringNode.create();
    }

    public SString(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SString(String str) {
        this(str.codePoints());
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return toStringDebug();
    }

    public String toStringDisplay() {
        return converter.execute(value);
    }

    public String toStringDebug() {
        return "\""
                + converter.execute(value)
                .replace(String.valueOf((char) 0x0007), "\\a")
                .replace("\b", "\\b")
                .replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace(String.valueOf((char) 0x000B), "\\v")
                .replace("\f", "\\f")
                .replace("\r", "\\r")
                .replace("\"", "\\\"")
                .replace("\\", "\\\\")
                + "\"";
    }
}
