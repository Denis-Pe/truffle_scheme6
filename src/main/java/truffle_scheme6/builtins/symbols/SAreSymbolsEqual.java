package truffle_scheme6.builtins.symbols;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SList;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;
import truffle_scheme6.runtime.SSymbol;

@BuiltinInfo(name = "symbol=?", lastVarArgs = true)
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SAreSymbolsEqual extends SBuiltin {
    @Specialization
    public Object doList(SList arg) {
        return switch (arg) {
            case SNil _nil -> throw new RuntimeException(ArityException.create(2, -1, 0));
            case SPair pair -> {
                var car = pair.getCar();
                if (!(car instanceof SSymbol)) {
                    throw new RuntimeException(UnsupportedTypeException.create(pair.toArray(), "%s is not a symbol".formatted(car.toString())));
                }

                var node = pair;
                while (node.getCdr() instanceof SPair next) {
                    if (car != next.getCar()) {
                        yield false;
                    }
                    node = next;
                }

                yield true;
            }
            default -> throw new RuntimeException("Unreachable");
        };
    }
}
