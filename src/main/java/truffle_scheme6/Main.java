package truffle_scheme6;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class Main {
    static Context context = Context.newBuilder().allowAllAccess(true).build();

    public static Value eval(String src) {
        return context.eval("scheme", src);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(eval("""
                (define (f x)
                  (+ x 42))
                (define (g p x)
                  (p x))
                (g f 23)
                """));

        System.out.println(eval("""
                (define (h op x y)
                  (op x y))
                (h + 23 42)
                (h * 23 42)
                """));
    }
}