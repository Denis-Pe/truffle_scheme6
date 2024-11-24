package truffle_scheme6;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.BeforeAll;

class BuiltInTest {
    static Context context;

    Value eval(String src) {
        return context.eval("scheme", src);
    }

    @BeforeAll
    static void setUp() {
        context = Context.newBuilder().allowAllAccess(true).build();
    }
}
