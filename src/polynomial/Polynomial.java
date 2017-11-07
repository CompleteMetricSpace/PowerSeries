package polynomial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import interfaces.Field;
import interfaces.IntegralDomain;

/**
 * 
 * @author KhAKhA
 *
 *         Polynomial class over a field F.
 * 
 *         Invariants: -
 *         <tt>coefs.get(k).isZero() == false<tt> for all <tt>k<tt>
 * @param <F>
 */
public class Polynomial<F extends IntegralDomain<F>> implements IntegralDomain<Polynomial<F>>
{
    private HashMap<Long, F> coefs;
    public F obj;

    public Polynomial(HashMap<Long, F> coefs, F obj)
    {
	this.coefs = coefs;
	this.obj = obj;
    }

    public Polynomial(F obj, F[] c)
    {
	HashMap<Long, F> map = new HashMap<>();
	for(int i = 0; i < c.length; i++)
	{
	    if(!c[i].isZero())
	        map.put(Long.valueOf(i), c[i]);
	}
	coefs = map;
	this.obj = obj;
    }
    
    public long degree()
    {
	Set<Long> set = coefs.keySet();
	if(set.isEmpty())
	    return -1;
	Iterator<Long> it = set.iterator();
	long n = 0;
	while(it.hasNext())
	{
	    long l = it.next();
	    n = n<l?l:n;
	}
	return n;
    }

    public F getCoef(long l)
    {
	return coefs.getOrDefault(l, obj.ZERO());
    }
    
    public F getLeadingCoef()
    {
	long n = degree();
	if(n == -1)
	    return obj.ZERO();
	return getCoef(n);
    }

    public Polynomial<F> add(Polynomial<F> b)
    {
	HashMap<Long, F> map = new HashMap<Long, F>();
	map.putAll(coefs);
	Iterator<Long> it = b.coefs.keySet().iterator();
	while(it.hasNext())
	{
	    long l = it.next();
	    F val = map.getOrDefault(l, obj.ZERO()).add(b.coefs.getOrDefault(l, obj.ZERO()));
	    if(val.isZero())
		map.remove(l);
	    else
		map.put(l, val);
	}
	return new Polynomial<F>(map, obj);
    }

    public Polynomial<F> sub(Polynomial<F> b)
    {
	HashMap<Long, F> map = new HashMap<Long, F>();
	map.putAll(coefs);
	Iterator<Long> it = b.coefs.keySet().iterator();
	while(it.hasNext())
	{
	    long l = it.next();
	    F val = map.getOrDefault(l, obj.ZERO()).sub(b.coefs.getOrDefault(l, obj.ZERO()));
	    if(val.isZero())
		map.remove(l);
	    else
		map.put(l, val);
	}
	return new Polynomial<F>(map, obj);
    }

    public Polynomial<F> mul(F c)
    {
	HashMap<Long, F> map = new HashMap<Long, F>();
	if(c.isZero())
	    return new Polynomial<F>(map, obj);
	Iterator<Long> aIt = this.coefs.keySet().iterator();
	while(aIt.hasNext())
	{
	    Long la = aIt.next();
	    F p = coefs.get(la).mul(c);
	    map.put(la, p);
	}
	return new Polynomial<F>(map, obj);
    }
    
    public Polynomial<F> mul(Polynomial<F> b)
    {
	HashMap<Long, F> map = new HashMap<Long, F>();
	Iterator<Long> aIt = this.coefs.keySet().iterator();
	Set<Long> bSet = b.coefs.keySet();
	while(aIt.hasNext())
	{
	    Long la = aIt.next();
	    Iterator<Long> bIt = bSet.iterator();
	    while(bIt.hasNext())
	    {
		Long lb = bIt.next();
		F s = map.getOrDefault(la+lb, obj.ZERO()).add(this.coefs.get(la).mul(b.coefs.get(lb)));
		if(s.isZero())
		    map.remove(la+lb);
		else
		    map.put(la+lb, s);
	    }
	}
	return new Polynomial<F>(map, obj);
    }

    @Override
    public Polynomial<F> ZERO()
    {
	return ZERO(obj);
    }

    @Override
    public Polynomial<F> ONE()
    {
	return ONE(obj);
    }
    
    @Override
    public Polynomial<F> NONE()
    {
	return NONE(obj);
    }

    @Override
    public boolean isZero()
    {
	return this.coefs.isEmpty();
    }
    
    public boolean equals(Object b)
    {
	if(!(b instanceof Polynomial<?>))
	    return false;
	Polynomial<?> p = (Polynomial<?>)b;
	return p.coefs.equals(this.coefs);
    }

    @Override
    public boolean isOne()
    {
	Set<Long> set = this.coefs.keySet();
	if(set.size() == 1 && set.contains(0L) && this.getCoef(0L).equals(obj.ONE()))
	    return true;
	return false;
    }
    
    @Override
    public Polynomial<F> negate()
    {
	return this.mul(obj.NONE());
    }
    
    public static <F extends Field<F>> Polynomial<F> monic(Polynomial<F> p)
    {
	if(p.isZero())
	    return p;
	return p.mul(p.getLeadingCoef().invert());
    }
    
    public static <F extends IntegralDomain<F>> Polynomial<F> ZERO(F obj)
    {
	HashMap<Long, F> map = new HashMap<>();
	return new Polynomial<F>(map, obj);
    }
    
    public static <F extends IntegralDomain<F>> Polynomial<F> ONE(F obj)
    {
	HashMap<Long, F> map = new HashMap<>();
	map.put(0L, obj.ONE());
	return new Polynomial<F>(map, obj);
    }
    
    public static <F extends IntegralDomain<F>> Polynomial<F> NONE(F obj)
    {
	HashMap<Long, F> map = new HashMap<>();
	map.put(0L, obj.NONE());
	return new Polynomial<F>(map, obj);
    }
    
    public static <F extends IntegralDomain<F>> Polynomial<F> MONOMIAL(long pow, F coef)
    {
	HashMap<Long, F> map = new HashMap<>();
	if(!coef.isZero())
	    map.put(pow, coef);
	return new Polynomial<F>(map, coef);
    }
    
    public static <F extends IntegralDomain<F>> Polynomial<F> LINEAR(F c0, F c1)
    {
	HashMap<Long, F> map = new HashMap<>();
	if(!c0.isZero())
	    map.put(0L, c0);
	if(!c1.isZero())
	    map.put(1L, c1);
	return new Polynomial<F>(map, c0);
    }

    public String toString()
    {
	String s = "[";
	long deg = this.degree();
	if(deg == -1)
	    return "[]";
	for(int i = 0;i<deg;i++)
	    s+=getCoef(i)+", ";
	s+=getCoef(deg)+"]";
	return s;
    }
}
