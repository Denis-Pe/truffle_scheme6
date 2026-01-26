package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.Iterator;
import java.util.NoSuchElementException;

@ExportLibrary(InteropLibrary.class)
public class SPair extends SList implements TruffleObject, Iterable<Object> {
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

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Object nth(long index) {
        var curr = this;

        var count = 0L;
        while (true) {
            var currCar = curr.car;
            var currCdr = curr.cdr;

            if (count == index) {
                return currCar;
            } else if (currCdr instanceof SPair nextPair) {
                curr = nextPair;
                count++;
            } else if (currCdr != SNil.SINGLETON) {
                throw new RuntimeException("Improper list");
            } else {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
        }
    }

    public Object nthImproper(long index) {
        var curr = this;

        var count = 0L;
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

    public void setNth(long index, Object value) {
        var curr = this;

        var count = 0L;
        while (true) {
            var currCdr = curr.cdr;

            if (count == index) {
                curr.setCar(value);
                return;
            } else if (currCdr instanceof SPair nextPair) {
                curr = nextPair;
                count++;
            } else if (currCdr != SNil.SINGLETON) {
                throw new RuntimeException("Improper list");
            } else {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
        }
    }

    public void setNthImproper(long index, Object value) {
        var curr = this;

        var count = 0L;
        while (true) {
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

    public boolean isProperList() {
        var curr = this;
        while (curr.cdr instanceof SPair next) curr = next;
        return curr.cdr == SNil.SINGLETON;
    }

    public long count() {
        var count = 0L;
        var curr = this;
        while (true) {
            count++;
            if (curr.cdr instanceof SPair next) curr = next;
            else if (curr.cdr == SNil.SINGLETON) break;
            else throw new RuntimeException("Improper list");
        }
        return count;
    }

    // will iterate through the whole thing even if it's improper,
    //  but won't include nil terminator if proper
    public Iterator<Object> improperIterator() {
        var outerThis = this;

        return new Iterator<Object>() {
            Object curr = outerThis;
            boolean seenLast = false;

            @Override
            public boolean hasNext() {
                return curr instanceof SPair || (curr != SNil.SINGLETON && !seenLast);
            }

            @Override
            public Object next() {
                if (curr instanceof SPair asPair) {
                    curr = asPair.cdr;
                    return asPair.car;
                } else if (curr != SNil.SINGLETON) {
                    seenLast = true;
                    return curr;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    // will throw an exception if the list is improper, but this iterator iterates the list twice
    // due to checking for whether it's improper or proper before any iteration
    public Iterator<Object> properIterator() {
        if (!isProperList()) {
            throw new RuntimeException("Improper list");
        } else {
            return improperIterator();
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return properIterator();
    }

    @Override
    public String toString() {
        return toStringList();
    }

    public String toStringPair() {
        return "(" + car + " . " + cdr + ")";
    }

    // if improper list, it will use dotted form,
    // still different from toStringPair() which will print as a literal pair
    public String toStringList() {
        StringBuilder sb = new StringBuilder("(");

        var curr = this;

        while (true) {
            sb.append(curr.car);

            if (curr.cdr instanceof SPair next) {
                curr = next;
            } else if (curr.cdr == SNil.SINGLETON) {
                break;
            } else {
                sb.append(" . ");
                sb.append(curr.cdr);
                break;
            }

            sb.append(" ");
        }

        sb.append(")");
        return sb.toString();
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

    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    long getArraySize() {
        return count();
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        return 0 <= index && index < count();
    }

    @ExportMessage
    Object readArrayElement(long index) throws InvalidArrayIndexException {
        try {
            return nth(index);
        } catch (IndexOutOfBoundsException e) {
            throw InvalidArrayIndexException.create(index, e);
        }
    }

    @ExportMessage
    boolean isArrayElementModifiable(long index) {
        return 0 <= index && index < count();
    }

    @ExportMessage
    void writeArrayElement(long index, Object value) throws InvalidArrayIndexException {
        try {
            setNth(index, value);
        } catch (IndexOutOfBoundsException e) {
            throw InvalidArrayIndexException.create(index, e);
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
    Iterator<Object> getIterator() {
        return iterator();
    }

    @ExportMessage
    Object toDisplayString(boolean allowSideEffects) {
        return this.toString();
    }
}
