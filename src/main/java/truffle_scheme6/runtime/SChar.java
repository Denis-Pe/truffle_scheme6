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
    private final int value;
    private static final TruffleString.FromJavaStringNode truffleStringFromJavaString = TruffleString.FromJavaStringNode.create();

    public SChar(int codepoint) {
        this.value = codepoint;
    }

    public SChar(char c) {
        this((int) c);
    }

    public int getValue() {
        return value;
    }

    @ExportMessage
    boolean isString() {
        return true;
    }

    @ExportMessage
    String asString() {
        return this.toString();
    }

    @ExportMessage
    TruffleString asTruffleString() {
        return truffleStringFromJavaString.execute(this.toString(), Constants.ENCODING);
    }

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "#\\" + Character.toString(value);
    }

}
