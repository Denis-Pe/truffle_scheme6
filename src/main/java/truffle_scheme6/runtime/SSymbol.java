package truffle_scheme6.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

// as per the standard section 1.1:
// > Symbols
// > Unlike strings, two symbols whose names are spelled the same way are never distinguishable.
// Therefore, the API for symbols at runtime has private constructors, with the static methods to create
// them hooking into a set of symbols to make sure that no two symbols have the same value
public class SSymbol {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    private static final Set<SSymbol> registeredSymbols = new HashSet<>();

    private SSymbol(TruffleString value) {
        this.value = value;
    }

    private SSymbol(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    private SSymbol(IntStream codepoints) {
        this(codepoints.toArray());
    }

    private SSymbol(String str) {
        this(str.codePoints());
    }

    public static SSymbol get(TruffleString value) {
        var rs = registeredSymbols.stream().filter(regs -> value.equals(regs.value)).findAny();

        if (rs.isPresent()) {
            return rs.get();
        } else {
            var s = new SSymbol(value);
            registeredSymbols.add(s);
            return s;
        }
    }

    public static SSymbol get(int[] codepoints) {
        var s = new SSymbol(codepoints);
        var rs = registeredSymbols.stream().filter(s::equals).findAny();

        if (rs.isPresent()) {
            return rs.get();
        } else {
            registeredSymbols.add(s);
            return s;
        }
    }

    public static SSymbol get(IntStream codepoints) {
        return SSymbol.get(codepoints.toArray());
    }

    public static SSymbol get(String str) {
        return SSymbol.get(str.codePoints());
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return converter.execute(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SSymbol that = (SSymbol) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
