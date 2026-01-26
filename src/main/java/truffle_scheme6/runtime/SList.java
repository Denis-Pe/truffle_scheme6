package truffle_scheme6.runtime;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.Iterator;

public abstract class SList {
    // nil WILL be automatically appended
    public static SList list(Object... objects) {
        if (objects.length == 0) {
            return SNil.SINGLETON;
        } else {
            var last = SNil.SINGLETON;
            var secondLast = objects[objects.length - 1];
            var curr = new SPair(secondLast, last);

            for (int i = objects.length - 2; i >= 0; i--) {
                curr = new SPair(objects[i], curr);
            }

            return curr;
        }
    }

    public abstract boolean isEmpty();
}
