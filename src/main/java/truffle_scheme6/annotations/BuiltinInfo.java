package truffle_scheme6.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface BuiltinInfo {
    String name() default "";

    boolean lastVarArgs() default false;
}
