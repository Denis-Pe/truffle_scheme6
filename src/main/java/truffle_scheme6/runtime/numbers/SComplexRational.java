package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.numerical_utils.ComparisonResult;

import java.math.BigDecimal;
import java.math.BigInteger;

public record SComplexRational(SFraction real, SFraction imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return this.isRealValued() ? real.compareTo(n) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return this.isRealValued() ? real.compareTo(n) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return this.isRealValued() ? real.compareTo(q) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return this.isRealValued() ? real.compareTo(q) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return this.isRealValued() ? real.compareTo(x) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return this.isRealValued() ? real.compareTo(x) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return this.isRealValued() ? real.compareTo(x) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        if (real.compareTo(z.real()) == ComparisonResult.Equal && imag.compareTo(z.imag()) == ComparisonResult.Equal) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        if (real.compareTo(z.real()) == ComparisonResult.Equal && imag.compareTo(z.imag()) == ComparisonResult.Equal) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        if (real.compareTo(z.real()) == ComparisonResult.Equal && imag.compareTo(z.imag()) == ComparisonResult.Equal) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        var compReal = switch (z.real()) {
            case SFractionBigInt fb -> real.compareTo(fb);
            case SFractionLong fl -> real.compareTo(fl);
            default -> throw new IllegalStateException("Unreachable branch: " + z.real());
        };

        var compImag = switch (z.imag()) {
            case SFractionBigInt fb -> real.compareTo(fb);
            case SFractionLong fl -> real.compareTo(fl);
            default -> throw new IllegalStateException("Unreachable branch: " + z.real());
        }; 
        
        if (compReal == ComparisonResult.Equal && compImag == ComparisonResult.Equal) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }
}
