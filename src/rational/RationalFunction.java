package rational;

import polynomial.Polynomial;
import polynomial.PolynomialDivision;
import polynomial.PolynomialGCD;
import interfaces.Field;

public class RationalFunction<F extends Field<F>> implements Field<RationalFunction<F>>
{
    Polynomial<F> num, den;
    
    
    public RationalFunction(Polynomial<F> n, Polynomial<F> d)
    {
	if(d.isZero())
	    throw new IllegalArgumentException("Denominator is zero");
	num = n;
	den = d;
	this.normalize();
    }
    
    private RationalFunction(Polynomial<F> n, Polynomial<F> d, boolean norm)
    {
	if(d.isZero())
	    throw new IllegalArgumentException("Denominator is zero");
	num = n;
	den = d;
	if(norm)
	    this.normalize();
    }
    
    @Override
    public RationalFunction<F> NONE()
    {
	return new RationalFunction<F>(num.NONE(), den.ONE());
    }

    @Override
    public RationalFunction<F> ZERO()
    {
	return new RationalFunction<F>(num.ZERO(), den.ONE());
    }

    @Override
    public RationalFunction<F> ONE()
    {
	return new RationalFunction<F>(num.ONE(), den.ONE());
    }
    
    public Polynomial<F> getNumerator()
    {
	return num;
    }
    
    public Polynomial<F> getDenominator()
    {
	return den;
    }
    
    private void normalize()
    {
	Polynomial<F> gcd = PolynomialGCD.polynomialGCD(num, den);
	num = PolynomialDivision.polynomialDivision(num, gcd).getFirst();
	den = PolynomialDivision.polynomialDivision(den, gcd).getFirst();
	if(num.isZero())
	    den = den.ONE();
	else
	{
	    F lc = num.getLeadingCoef().invert();
	    num = num.mul(lc);
	    den = den.mul(lc);
	}
    }
    
    public boolean equals(Object b)
    {
	if(!(b instanceof RationalFunction<?>))
	    return false;
	RationalFunction<?> r = (RationalFunction<?>)b;
	return r.getNumerator().equals(this.getNumerator()) && r.getDenominator().equals(this.getDenominator());
    }

    @Override
    public RationalFunction<F> add(RationalFunction<F> b)
    {
	return new RationalFunction<>(num.mul(b.getDenominator()).add(den.mul(b.getNumerator())), den.mul(b.getDenominator()));
    }

    @Override
    public RationalFunction<F> sub(RationalFunction<F> b)
    {
	return new RationalFunction<>(num.mul(b.getDenominator()).sub(den.mul(b.getNumerator())), den.mul(b.getDenominator()));
    }

    @Override
    public RationalFunction<F> mul(RationalFunction<F> b)
    {
	return new RationalFunction<>(num.mul(b.getNumerator()), den.mul(b.getDenominator()));
    }

    @Override
    public RationalFunction<F> negate()
    {
	return new RationalFunction<>(num, den.negate(), false);
    }

    @Override
    public boolean isZero()
    {
	return num.isZero();
    }

    @Override
    public boolean isOne()
    {
	return num.isOne() && den.isOne();
    }

    @Override
    public RationalFunction<F> div(RationalFunction<F> b)
    {
	return new RationalFunction<>(num.mul(b.getDenominator()), den.mul(b.getNumerator()));
    }

    @Override
    public RationalFunction<F> invert()
    {
	return new RationalFunction<>(den, num);
    }

    @Override
    public RationalFunction<F> pow(long p)
    {
	if(p == 0)
	    return ONE();
	if(p < 0)
	    return this.invert().pow(-p);
	if(p % 2 == 0)
	    return this.mul(this).pow(p / 2);
	return this.mul(this.pow(p - 1));
    }
    
    public String toString()
    {
	return num+"/"+den;
    }

}
