package misc;

import number.BigRational;
import interfaces.Field;
import interfaces.IntegralDomain;

public class QuotientField<D extends IntegralDomain<D>> implements Field<QuotientField<D>>
{
    private D num, den;

    public QuotientField(D n, D d)
    {
	num = n;
	den = d;
	if(d.isZero())
	    throw new IllegalArgumentException("Denominator is zero");
    }

    @Override
    public QuotientField<D> ZERO()
    {
	return new QuotientField<>(num.ZERO(), den.ONE());
    }

    @Override
    public QuotientField<D> ONE()
    {
	return new QuotientField<>(num.ZERO(), den.ONE());
    }

    public D getNumerator()
    {
	return num;
    }

    public D getDenominator()
    {
	return den;
    }

    @Override
    public QuotientField<D> add(QuotientField<D> b)
    {
	return new QuotientField<>(num.mul(b.den).add(b.num.mul(den)), den.mul(b.den));
    }

    @Override
    public QuotientField<D> sub(QuotientField<D> b)
    {
	return new QuotientField<>(num.mul(b.den).sub(b.num.mul(den)), den.mul(b.den));
    }

    @Override
    public QuotientField<D> mul(QuotientField<D> b)
    {
	return new QuotientField<>(num.mul(b.num), den.mul(b.den));
    }

    @Override
    public boolean isZero()
    {
	return num.isZero();
    }

    @Override
    public boolean isOne()
    {
	return num.equals(den);
    }

    @Override
    public QuotientField<D> NONE()
    {
	return new QuotientField<>(num.NONE(), num.ONE());
    }

    @Override
    public QuotientField<D> div(QuotientField<D> b)
    {
	return new QuotientField<>(num.mul(b.den), den.mul(b.num));
    }

    @Override
    public QuotientField<D> invert()
    {
	return new QuotientField<>(den, num);
    }

    @Override
    public QuotientField<D> negate()
    {
	return new QuotientField<>(num.negate(), den);
    }

    @Override
    public QuotientField<D> pow(long p)
    {
	if(p == 0)
	    return ONE();
	if(p < 0)
	    return this.invert().pow(-p);
	if(p % 2 == 0)
	    return this.mul(this).pow(p / 2);
	return this.mul(this.pow(p - 1));
    }

    public boolean equals(Object b)
    {
	if(!(b instanceof QuotientField<?>))
	    return false;
	QuotientField<?> q = (QuotientField<?>) b;
	if(!q.getNumerator().getClass().equals(num.getClass()))
	    return false;
	return this.getNumerator().mul((D) q.getDenominator()).
		equals(this.getDenominator().mul((D) q.getNumerator()));
    }
}
