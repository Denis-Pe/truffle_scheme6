package p.denis.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import p.denis.Constants;
import p.denis.SchemeObject;

public class SCharacter extends SchemeObject {
    // todo one character string for now. Will look into turning this into a byte[] later,
    //  for now I don't want to spend much time on it
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();


    public SCharacter(char c) {
        this((int) c);
    }

    public SCharacter(int unsignedCodepointInt) {
        var builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        appender.execute(builder, unsignedCodepointInt);

        this.value = builder.toStringUncached();
    }


    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
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
        return "#\\" + converter.execute(value);
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(this.getClass().getSimpleName(), this.toString());
    }
}
