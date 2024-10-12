package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

@ExportLibrary(InteropLibrary.class)
public class SChar implements TruffleObject {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SChar(int codepoint) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        appender.execute(builder, codepoint);


        this.value = builder.toStringUncached();
    }

    public SChar(char c) {
        this((int) c);
    }

    public SChar(TruffleString value) {
        this.value = value;
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
    String toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "#\\" + value.toJavaStringUncached();
    }

}
