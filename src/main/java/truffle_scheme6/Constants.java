package truffle_scheme6;

import com.oracle.truffle.api.strings.TruffleString;
import truffle_scheme6.runtime.SNil;

public class Constants {
    public static final TruffleString.Encoding ENCODING = TruffleString.Encoding.UTF_8;
    public static final Object UNSPECIFIED = SNil.SINGLETON;
}
