package truffle_scheme6.builtins.characters;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SChar;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFraction;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

import java.math.BigInteger;

@BuiltinInfo(name = "integer->char")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SIntegerToChar extends SBuiltin {
    private static final BigInteger D7FF = BigInteger.valueOf(0xD7FF);
    private static final BigInteger E000 = BigInteger.valueOf(0xE000);
    private static final BigInteger TEN_FFFF = BigInteger.valueOf(0x10FFFF);

    @Specialization
    public SChar doLong(long arg) {
        if (arg >= 0 && arg <= 0xD7FF || arg >= 0xE000 && arg <= 0x10FFFF) {
            return new SChar((int) arg);
        } else {
            throw new IllegalArgumentException("arg must be between 0 and 0xD7FF or 0xE000 and 0x10FFFF");
        }
    }

    @Specialization
    public SChar doBigInt(SBigInt arg) {
        BigInteger v = arg.value();
        if (v.compareTo(BigInteger.ZERO) >= 0 && v.compareTo(E000) <= 0 || v.compareTo(E000) >= 0 && v.compareTo(TEN_FFFF) <= 0) {
            return new SChar(arg.value().intValue());
        } else {
            throw new IllegalArgumentException("arg must be between 0 and 0xD7FF or 0xE000 and 0x10FFFF");
        }
    }

    @Specialization
    public SChar doLFraction(SFractionLong arg) {
        if (!arg.isPerfectlyDivisible()) {
            throw new IllegalArgumentException("arg must be an integer");
        }
        var v = arg.longValue();
        return doLong(v);
    }

    @Specialization
    public SChar doBFraction(SFractionBigInt arg) {
        if (!arg.isPerfectlyDivisible()) {
            throw new IllegalArgumentException("arg must be an integer");
        }
        var v = arg.bigIntValue();
        return doBigInt(v);
    }
}
