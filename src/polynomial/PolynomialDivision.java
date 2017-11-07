package polynomial;

import java.util.HashMap;
import java.util.Hashtable;

import misc.Pair;
import interfaces.Field;

public class PolynomialDivision
{
    public static <F extends Field<F>> Pair<Polynomial<F>, Polynomial<F>> polynomialDivision(Polynomial<F> f, Polynomial<F> g)
    {
	if(g.isZero())
	    throw new IllegalArgumentException("Cannot divide by 0-Polynomial");
	Polynomial<F> r = f;
	Polynomial<F> q = f.ZERO();
	long gDeg = g.degree();
	F gLc = g.getCoef(gDeg);
	long rDeg = r.degree();
	while(rDeg >= gDeg)
	{
	    F rLc = r.getCoef(rDeg);
	    Polynomial<F> monom = Polynomial.MONOMIAL(rDeg-gDeg, rLc.div(gLc));
	    q = q.add(monom);
	    r = r.sub(g.mul(monom));
	    rDeg = r.degree();
	}
	return new Pair<Polynomial<F>, Polynomial<F>>(q, r);
    }
    
    public static <F extends Field<F>> Polynomial<F> expandInAPoint(Polynomial<F> f, F a)
    {
	Polynomial<F> q = f;
	HashMap<Long, F> map = new HashMap<>();
	Polynomial<F> linear = Polynomial.LINEAR(a.negate(), a.ONE());
	Long d = 0L;
	while(!q.isZero())
	{
	    Pair<Polynomial<F>, Polynomial<F>> division = polynomialDivision(q, linear);
	    Polynomial<F> r = division.getSecond();
	    if(!r.isZero())
		map.put(d, r.getCoef(0L));
	    q = division.getFirst();
	    d++;
	}
	return new Polynomial<F>(map, f.obj);
    }
}
