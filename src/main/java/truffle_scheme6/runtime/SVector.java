package truffle_scheme6.runtime;

import com.oracle.truffle.api.ArrayUtils;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import org.graalvm.polyglot.HostAccess;
import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@ExportLibrary(InteropLibrary.class)
public class SVector implements TruffleObject {
    private final Object[] elms;

    public SVector(Object[] elms) {
        this.elms = elms;
    }

    public Object[] getElms() {
        return elms;
    }

    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    long getArraySize() {
        return elms.length;
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        return 0 <= index && index < elms.length;
    }

    @ExportMessage
    Object readArrayElement(long index) throws InvalidArrayIndexException {
        try {
            return elms[Math.toIntExact(index)];
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            throw InvalidArrayIndexException.create(index);
        }
    }

    @ExportMessage
    boolean isArrayElementModifiable(long index) {
        return 0 <= index && index < elms.length;
    }

    @ExportMessage
    void writeArrayElement(long index, Object value) throws InvalidArrayIndexException {
        try {
            elms[Math.toIntExact(index)] = value;
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            throw InvalidArrayIndexException.create(index);

        }
    }

    @ExportMessage
    boolean isArrayElementInsertable(long index) {
        return false;
    }

    @ExportMessage
    boolean hasIterator() {
        return true;
    }

    @ExportMessage
    Object getIterator() {
        return Arrays.asList(elms).iterator();
    }

    @ExportMessage
    Object toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "#(" + StringFormatting.separatedBy(" ", elms) + ")";
    }
}
