package polynomial;

import interfaces.Field;
import misc.Pair;

public class PolynomialGCD
{
    public static <F extends Field<F>> Polynomial<F> polynomialGCD(Polynomial<F> f, Polynomial<F> g)
    {
	if(g.isZero())
	{
	    if(f.isZero())
		return g.ONE();
	    else
		return polynomialGCD(g, f);
	}
	Polynomial<F> b = f;
	Polynomial<F> r = g;
	while(!r.isZero())
	{
	    Pair<Polynomial<F>, Polynomial<F>> qr = PolynomialDivision.polynomialDivision(b, r);
	    b = r;
	    r = qr.getSecond();
	}
	return Polynomial.monic(b);
    }
    
    public static <F extends Field<F>> Pair<Pair<Polynomial<F>, Polynomial<F>>, Polynomial<F>> polynomialExtendedGCD(Polynomial<F> f, Polynomial<F> g)
    {
	Polynomial<F> a1 = f.ONE(), a2 = f.ZERO();
	Polynomial<F> b1 = f.ZERO(), b2 = f.ONE();
	while(!g.isZero())
	{
	    Pair<Polynomial<F>, Polynomial<F>> division = PolynomialDivision.polynomialDivision(f, g);
	    Polynomial<F> q = division.getFirst(), r = division.getSecond();
	    f = g;
	    g = r;
	    Polynomial<F> r1 = a1.sub(q.mul(b1)), r2 = a2.sub(q.mul(b2));
	    a1 = b1;
	    a2 = b2;
	    b1 = r1;
	    b2 = r2;
	}
	//Make monic
	F lcInverse = f.getLeadingCoef().invert();
	a1 = a1.mul(lcInverse);
	a2 = a2.mul(lcInverse);
	f = f.mul(lcInverse);
	return new Pair<>(new Pair<>(a1, a2), f);
    }
    
    public static <F extends Field<F>> Pair<Polynomial<F>, Polynomial<F>> polynomialHalfExtendedGCD(Polynomial<F> f, Polynomial<F> g)
    {
	Polynomial<F> a1 = f.ONE();
	Polynomial<F> b1 = f.ZERO();
	while(!g.isZero())
	{
	    Pair<Polynomial<F>, Polynomial<F>> division = PolynomialDivision.polynomialDivision(f, g);
	    Polynomial<F> q = division.getFirst(), r = division.getSecond();
	    f = g;
	    g = r;
	    Polynomial<F> r1 = a1.sub(q.mul(b1));
	    a1 = b1;
	    b1 = r1;
	}
	//Make monic
	F lcInverse = f.getLeadingCoef().invert();
	a1 = a1.mul(lcInverse);
	f = f.mul(lcInverse);
	return new Pair<>(a1, f);
    }
}
