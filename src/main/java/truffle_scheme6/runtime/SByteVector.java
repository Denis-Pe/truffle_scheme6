package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import truffle_scheme6.utils.StringFormatting;

import java.util.Iterator;

@ExportLibrary(InteropLibrary.class)
public class SByteVector implements TruffleObject {
    private final byte[] bytes;

    public SByteVector(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    long getArraySize() {
        return bytes.length;
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        return 0 <= index && index < bytes.length;
    }

    @ExportMessage
    Object readArrayElement(long index) throws InvalidArrayIndexException {
        try {
            return bytes[Math.toIntExact(index)];
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            throw InvalidArrayIndexException.create(index);
        }
    }

    @ExportMessage
    boolean isArrayElementModifiable(long index) {
        return isArrayElementReadable(index);
    }

    @ExportMessage
    void writeArrayElement(long index, Object value) throws UnsupportedTypeException, InvalidArrayIndexException {
        try {
            bytes[Math.toIntExact(index)] = (Byte) value;
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            throw InvalidArrayIndexException.create(index);
        } catch (ClassCastException e) {
            throw UnsupportedTypeException.create(new Object[]{value}, "Value given is not a byte");
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
        // there's no easy way to do it with byte[] :)
        return new Iterator<Byte>() {
            byte[] elms = bytes;
            int i = 0;

            @Override
            public boolean hasNext() {
                return 0 <= i && i < elms.length;
            }

            @Override
            public Byte next() {
                return elms[i++];
            }
        };
    }

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "#vu8(" + StringFormatting.spaced(bytes) + ")";
    }
}
