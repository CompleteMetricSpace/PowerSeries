package series;

import interfaces.Field;
import interfaces.IntegralDomain;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import polynomial.Polynomial;
import number.BigRational;
/**
 * 
 * @author KhAKhA
 * Power Series with lazy evaluation 
 */
public class PowerSeries<F extends Field<F>> implements IntegralDomain<PowerSeries<F>>
{
    public static Long MAX_TERMS = 30L;
    
    private Function<Long, F> f;
    private HashMap<Long, F> vals;
    F obj;

    public PowerSeries(Function<Long, F> f, F c)
    {
	this.f = f;
	vals = new HashMap<>();
	this.obj = c;
    }
    
    public PowerSeries(Function<Long, F> f, HashMap<Long, F> map, F c)
    {
	this.f = f;
	vals = map;
	this.obj = c;
    }
    
    public PowerSeries(Polynomial<F> p, F c)
    {
	this.f = n -> p.getCoef(n);
	vals = new HashMap<>();
	this.obj = c;
    }

    public PowerSeries(Polynomial<F> p)
    {
	this(p, p.obj);
    }
    
    public F getCoef(Long k)
    {
	if(vals.containsKey(k))
	    return vals.get(k);
	F c = f.apply(k);
	vals.put(k, c);
	return c;
    }

    public boolean isInvertible()
    {
	return !getCoef(0L).isZero();
    }
    
    public long getNumberOfEvaluatedCoefs()
    {
	return vals.size();
    }
    
    /**
     * Shifts the power series
     * @param s an integer
     * @return shifted series (to the right)
     */
    public PowerSeries<F> shift(Long s)
    {
	return new PowerSeries<F>(n -> n<s?obj.ZERO():this.getCoef(n-s), obj);
    }

    /**
     * Order of a power series
     * @return null if power series .isProbableZero() is true or the order of the power series 
     */
    public Long order()
    {
	for(Long i = 0L;i<MAX_TERMS;i++)
	{
	    if(!getCoef(i).isZero())
		return i;
	}
	return null;
    }
    
    public PowerSeries<F> add(PowerSeries<F> b)
    {
	return new PowerSeries<F>(n -> this.getCoef(n).add(b.getCoef(n)), obj);
    }

    public PowerSeries<F> sub(PowerSeries<F> b)
    {
	return new PowerSeries<F>(n -> this.getCoef(n).sub(b.getCoef(n)), obj);
    }

    public PowerSeries<F> mul(PowerSeries<F> b)
    {
	Function<Long, F> g = k -> {
	    F sum = obj.ZERO();
	    for(Long i = 0L;i<=k;i++)
		sum = sum.add(this.getCoef(i).mul(b.getCoef(k-i)));
	    return sum;
	};
	return new PowerSeries<F>(g, obj);
    }
    
    public PowerSeries<F> add(F b)
    {
	return new PowerSeries<F>(n -> n==0L?this.getCoef(n).add(b):this.getCoef(n), obj);
    }

    public PowerSeries<F> sub(F b)
    {
	return new PowerSeries<F>(n -> n==0L?this.getCoef(n).sub(b):this.getCoef(n), obj);
    }

    public PowerSeries<F> mul(F b)
    {
	return new PowerSeries<F>(n -> this.getCoef(n).mul(b), obj);
    }
    
    public PowerSeries<F> invert()
    {
	if(!isInvertible())
	    throw new IllegalStateException("PowerSeries cannot be inverted");
	F a = this.getCoef(0L).pow(-1);
	HashMap<Long, F> bTable = new HashMap<>();
	bTable.put(0L, a);
	BiFunction<BiFunction, Long, F> g = (s, k) -> {
	    if(bTable.containsKey(k))
		return bTable.get(k);
	    F sum = obj.ZERO();
	    for(Long n =0L;n<k;n++)
		sum = sum.add(this.getCoef(k-n).mul((F)s.apply(s, n)));
	    F b = sum.mul(a.mul(obj.NONE()));
	    bTable.put(k, b);
	    return b;
	};
	Function<Long, F> h = k -> g.apply(g, k);
	return new PowerSeries<F>(h, bTable, obj);
    }
    
    public String toString(Long k)
    {
	String s = "[";
	Long i = 0L;
	while(i.compareTo(k)<0)
	{
	    s += this.getCoef(i)+", ";
	    i++;
	}
	s+= this.getCoef(k)+"]";
	return s;
    }
    
    public static void main(String[] args)
    {
	Function<Long, BigRational> g = n -> BigRational.ONE.div(new BigRational(n.intValue()+1));
	PowerSeries<BigRational> ps = new PowerSeries<>(g, BigRational.ZERO);
	PowerSeries<BigRational> psi = ps.invert();
	//ArrayList<Integer> list = new ArrayList<>();
	//list.set(5, 1);
	System.out.println(ps.toString(10L));
	System.out.println(psi.toString(10L));
	System.out.println(ps.mul(psi).toString(10L));
	System.out.println(psi.getNumberOfEvaluatedCoefs());
	System.out.println(ps.getNumberOfEvaluatedCoefs());
	System.out.println(psi.vals.keySet());
	System.out.println(ps.vals.keySet());
	
    }

    @Override
    public PowerSeries<F> NONE()
    {
	return new PowerSeries<F>(n -> n==0L?obj.NONE():obj.ZERO(), obj);
    }

    @Override
    public PowerSeries<F> ZERO()
    {
	return new PowerSeries<F>(n -> obj.ZERO(), obj);
    }

    @Override
    public PowerSeries<F> ONE()
    {
	return new PowerSeries<F>(n -> n==0L?obj.ONE():obj.ZERO(), obj);
    }

    @Override
    public PowerSeries<F> negate()
    {
	return new PowerSeries<F>(n -> this.getCoef(n).negate(), obj);
    }

    /**
     * Not implemented
     */
    @Override
    public boolean isZero()
    {
	return false;
    }
    
    public boolean isProbableZero()
    {
	for(Long i = 0L;i<MAX_TERMS;i++)
	{
	    if(!getCoef(i).isZero())
		return false;
	}
	return true;
    }

    /**
     * Not implemented
     */
    @Override
    public boolean isOne()
    {
	return false;
    }
    
    public boolean isProbableOne()
    {
	if(!getCoef(0L).isOne())
	    return false;
	for(Long i = 1L;i<MAX_TERMS;i++)
	{
	    if(!getCoef(i).isZero())
		return false;
	}
	return true;
    }

}
