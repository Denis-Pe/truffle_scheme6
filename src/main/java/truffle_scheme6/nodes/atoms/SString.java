package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeObject;

import java.util.stream.IntStream;

public final class SString extends SchemeObject {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SString(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public SString(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SString(String str) {
        this(str.codePoints());
    }

    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        return this;
    }

    @Override
    public String toString() {
        return "\"" + converter.execute(value) + "\"";
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(this.getClass().getSimpleName(), this.toString());
    }
}
