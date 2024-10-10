package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class SNil implements TruffleObject {
    public static final SNil SINGLETON = new SNil();

    private SNil() {
    }

    @ExportMessage
    boolean isNull() {
        return true;
    }

    @ExportMessage
    Object toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "()";
    }
}
