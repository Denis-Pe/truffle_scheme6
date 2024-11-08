package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.private_utils.ComparisonResult;

import java.math.BigDecimal;
import java.math.BigInteger;

public record SComplexFloat(float real, float imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag == 0.0f;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return this.isRealValued() ? ComparisonResult.from(Float.compare(real, n)) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return this.isRealValued() ? ComparisonResult.from(Float.compare(real, n.floatValue())) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return this.isRealValued() ? ComparisonResult.from(Float.compare(real, q.floatValue())) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return this.isRealValued() ? ComparisonResult.from(Float.compare(real, q.floatValue())) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return this.isRealValued() ? ComparisonResult.from(Float.compare(real, x)) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return this.isRealValued() ? ComparisonResult.from(Double.compare(real, x)) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return this.isRealValued() ? ComparisonResult.from(BigDecimal.valueOf(real).compareTo(x)) : ComparisonResult.Unequal;
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        var compReal = BigDecimal.valueOf(real).compareTo(z.real());
        var compImag = BigDecimal.valueOf(imag).compareTo(z.imag());
        
        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        var compReal = Double.compare(real, z.real());
        var compImag = Double.compare(imag, z.imag());

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        var compReal = Float.compare(real, z.real());
        var compImag = Float.compare(imag, z.imag());

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        var compReal = Float.compare(real, z.real().floatValue());
        var compImag = Float.compare(imag, z.imag().floatValue());

        if (compReal == 0 && compImag == 0) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }
}
