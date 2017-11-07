package algebraic;

import polynomial.Polynomial;
import polynomial.PolynomialDivision;
import polynomial.PolynomialGCD;
import interfaces.Field;

public class AlgebraicNumber<F extends Field<F>> implements Field<AlgebraicNumber<F>>
{
    Polynomial<F> minPoly;
    Polynomial<F> number;
    F obj;
    
    public AlgebraicNumber(Polynomial<F> number, Polynomial<F> minPoly)
    {
	this.minPoly = Polynomial.monic(minPoly);
	this.number = number;
	this.obj = minPoly.obj;
	simplify();
    }
    
    private void simplify()
    {
	number = PolynomialDivision.polynomialDivision(number, minPoly).getSecond();
    }
    
    public Polynomial<F> getMinimalPolynomial()
    {
	return minPoly;
    }
    
    public Polynomial<F> getNumber()
    {
	return number;
    }
    
    @Override
    public AlgebraicNumber<F> NONE()
    {
	return new AlgebraicNumber<F>(number.NONE(), minPoly);
    }

    @Override
    public AlgebraicNumber<F> ZERO()
    {
	return new AlgebraicNumber<F>(number.ZERO(), minPoly);
    }

    @Override
    public AlgebraicNumber<F> ONE()
    {
	return new AlgebraicNumber<F>(number.ONE(), minPoly);
    }

    @Override
    public AlgebraicNumber<F> add(AlgebraicNumber<F> b)
    {
	if(!this.getMinimalPolynomial().equals(b.getMinimalPolynomial()))
	    throw new IllegalArgumentException("Different Extensions");
	return new AlgebraicNumber<F>(this.getNumber().add(b.getNumber()), this.getMinimalPolynomial());
    }

    @Override
    public AlgebraicNumber<F> sub(AlgebraicNumber<F> b)
    {
	if(!this.getMinimalPolynomial().equals(b.getMinimalPolynomial()))
	    throw new IllegalArgumentException("Different Extensions");
	return new AlgebraicNumber<F>(this.getNumber().sub(b.getNumber()), this.getMinimalPolynomial());
    }

    @Override
    public AlgebraicNumber<F> mul(AlgebraicNumber<F> b)
    {
	if(!this.getMinimalPolynomial().equals(b.getMinimalPolynomial()))
	    throw new IllegalArgumentException("Different Extensions");
	return new AlgebraicNumber<F>(this.getNumber().mul(b.getNumber()), this.getMinimalPolynomial());
    }

    @Override
    public AlgebraicNumber<F> negate()
    {
	return new AlgebraicNumber<F>(number.negate(), minPoly);
    }

    @Override
    public boolean isZero()
    {
	return number.isZero();
    }

    @Override
    public boolean isOne()
    {
	return number.isOne();
    }

    @Override
    public AlgebraicNumber<F> div(AlgebraicNumber<F> b)
    {
	if(!this.getMinimalPolynomial().equals(b.getMinimalPolynomial()))
	    throw new IllegalArgumentException("Different Extensions");
	return this.mul(b.invert());
    }

    @Override
    public AlgebraicNumber<F> invert()
    {
	if(this.isZero())
	    throw new IllegalStateException("Number is zero");
	Polynomial<F> s = PolynomialGCD.polynomialHalfExtendedGCD(number, minPoly).getFirst();
	return new AlgebraicNumber<F>(s, minPoly);
    }

    @Override
    public AlgebraicNumber<F> pow(long p)
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
	return "AN("+number+"|"+minPoly+")";
    }
}
