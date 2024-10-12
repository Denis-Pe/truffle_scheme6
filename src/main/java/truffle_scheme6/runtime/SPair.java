package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.*;

@ExportLibrary(InteropLibrary.class)
public class SPair implements TruffleObject {
    private Object car;
    private Object cdr;

    // nil will NOT be automatically appended
    public static SPair from(Object... objects) {
        if (objects.length < 2) {
            throw new IllegalArgumentException("Can't create pair from empty array");
        } else {
            var last = objects[objects.length - 1];
            var secondLast = objects[objects.length - 2];
            var curr = new SPair(secondLast, last);

            for (int i = objects.length - 3; i >= 0; i--) {
                curr = new SPair(objects[i], curr);
            }

            return curr;
        }
    }

    public SPair(Object car, Object cdr) {
        if (car == null || cdr == null) {
            throw new IllegalArgumentException("Can't create pair using Java null");
        }
        this.car = car;
        this.cdr = cdr;
    }

    public Object getCar() {
        return car;
    }

    public Object getCdr() {
        return cdr;
    }

    public void setCar(Object car) {
        if (car == null) {
            throw new IllegalArgumentException("Can't set car to Java null");
        }
        this.car = car;
    }

    public void setCdr(Object cdr) {
        if (cdr == null) {
            throw new IllegalArgumentException("Can't set cdr to Java null");
        }
        this.cdr = cdr;
    }

    public Object nth(int index) {
        var curr = this;

        int count = 0;
        while (true) {
            var currCar = curr.car;
            var currCdr = curr.cdr;

            if (count == index) {
                return currCar;
            } else if (currCdr instanceof SPair nextPair) {
                curr = nextPair;
                count++;
            } else if (currCdr != SNil.SINGLETON && count + 1 == index) {
                return currCdr;
            } else {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
        }
    }

    public void setNth(int index, Object value) {
        var curr = this;

        int count = 0;
        while (true) {
            var currCar = curr.car;
            var currCdr = curr.cdr;

            if (count == index) {
                curr.setCar(value);
                return;
            } else if (currCdr instanceof SPair nextPair) {
                curr = nextPair;
                count++;
            } else if (currCdr != SNil.SINGLETON && count + 1 == index) {
                curr.setCdr(value);
                return;
            } else {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
        }
    }

    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    long getArraySize() {
        long count = 1;

        var curr = this;
        while (true) {
            var currCdr = curr.cdr;

            if (currCdr instanceof SPair nextCurr) {
                curr = nextCurr;
                count++;
            } else if (currCdr != SNil.SINGLETON) {
                // todo Chez implementation of the length fn raises an exception in this case:
                //  worth reconsidering whether or not interop should be able to count improper lists
                count++;
                break;
            } else {
                break;
            }
        }

        return count;
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        try {
            var _val = nth(Math.toIntExact(index));
            return true;
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            return false;
        }
    }

    @ExportMessage
    Object readArrayElement(long index) throws InvalidArrayIndexException {
        try {
            return nth(Math.toIntExact(index));
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            throw InvalidArrayIndexException.create(index);
        }
    }

    @ExportMessage
    boolean isArrayElementModifiable(long index) {
        try {
            var _val = nth(Math.toIntExact(index));
            return true;
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            return false;
        }
    }

    @ExportMessage
    void writeArrayElement(long index, Object value) throws InvalidArrayIndexException {
        try {
            setNth(Math.toIntExact(index), value);
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
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
        var outerThis = this;

        return new Iterator<Object>() {
            Object curr = outerThis;

            @Override
            public boolean hasNext() {
                return curr instanceof SPair || curr != SNil.SINGLETON;
            }

            @Override
            public Object next() {
                if (curr instanceof SPair asPair) {
                    curr = asPair.cdr;
                    return asPair.car;
                } else if (curr != SNil.SINGLETON) {
                    return curr;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @ExportMessage
    Object toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }

    @Override
    public String toString() {
        return "(" + car + " . " + cdr + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SPair sPair = (SPair) o;
        return car.equals(sPair.car) && cdr.equals(sPair.cdr);
    }

    @Override
    public int hashCode() {
        int result = car.hashCode();
        result = 31 * result + cdr.hashCode();
        return result;
    }
}
