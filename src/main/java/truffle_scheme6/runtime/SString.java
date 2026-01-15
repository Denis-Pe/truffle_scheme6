package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

import java.util.stream.IntStream;

@ExportLibrary(InteropLibrary.class)
public class SString implements TruffleObject {
    private final TruffleString value;
    private final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();
    ;

    private static final TruffleStringBuilder.AppendCodePointNode builderAppend = TruffleStringBuilder.AppendCodePointNode.create();
    private static final TruffleStringBuilder.ToStringNode builderToString = TruffleStringBuilder.ToStringNode.create();

    public SString(TruffleString value) {
        this.value = value;
    }

    public SString(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            builderAppend.execute(builder, c);
        }

        this.value = builderToString.execute(builder);
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

    @ExportMessage
    boolean isString() {
        return true;
    }

    @ExportMessage
    String asString() {
        return converter.execute(value);
    }

    @ExportMessage
    TruffleString asTruffleString() {
        return value;
    }

    @ExportMessage
    Object toDisplayString(boolean allowSideEffects) {
        return this.toString();
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
