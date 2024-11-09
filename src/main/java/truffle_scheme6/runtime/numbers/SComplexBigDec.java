package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.numerical_utils.ComparisonResult;

import java.math.BigDecimal;
import java.math.BigInteger;

public record SComplexBigDec(BigDecimal real, BigDecimal imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(BigDecimal.valueOf(n))) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(new BigDecimal(n))) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(q.bigDecimalValue())) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(q.bigDecimalValue())) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(BigDecimal.valueOf(x))) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(BigDecimal.valueOf(x))) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return this.isRealValued() ? ComparisonResult.from(real.compareTo(x)) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        var compReal = real.compareTo(z.real);
        var compImag = imag.compareTo(z.imag);

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        var compReal = real.compareTo(BigDecimal.valueOf(z.real()));
        var compImag = imag.compareTo(BigDecimal.valueOf(z.imag()));

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        var compReal = real.compareTo(BigDecimal.valueOf(z.real()));
        var compImag = imag.compareTo(BigDecimal.valueOf(z.imag()));

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        var compReal = real.compareTo(z.real().bigDecimalValue());
        var compImag = real.compareTo(z.imag().bigDecimalValue());

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }
}
