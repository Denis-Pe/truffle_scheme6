package truffle_scheme6.nodes.atoms.numbers.integers;

import truffle_scheme6.nodes.atoms.numbers.SNumberLiteralNode;

import java.math.BigInteger;

public abstract class SIntegerLiteralNode extends SNumberLiteralNode {
    public abstract BigInteger asBigInteger();

    public abstract long asLong();

    public abstract boolean isOne();
}
