package p.denis.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import p.denis.Constants;
import p.denis.SchemeObject;

import java.util.stream.IntStream;

public final class Identifier extends SchemeObject {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public Identifier(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public Identifier(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public Identifier(String javaString) {
        this(javaString.codePoints());
    }



    @Override
    public SchemeObject execute(VirtualFrame frame) {
        // todo
        return null;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        return this;
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return converter.execute(value);
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(this.getClass().getSimpleName(), this.toString());
    }
}
