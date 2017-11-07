package number;

/**
 *  Compilation:  javac BigRational.java
 *  Execution:    java BigRational
 *
 *  Immutable ADT for arbitrarily large Rational numbers. 
 * 
 *  Invariants
 *  ----------
 *   -  gcd(num, den) = 1, i.e., rational number is in reduced form
 *   -  den >= 1, i.e., the denominator is always a positive integer
 *   -  0/1 is the unique representation of zero
 *
 *  % java BigRational
 *  5/6
 *  1
 *  1/120000000
 *  1073741789/12
 *  1
 *  841/961
 *  -1/3
 *  0
 *  true
 *  Exception in thread "main" java.lang.ArithmeticException: Denominator is zero
 *
 ******************************************************************************/

import interfaces.Field;
import interfaces.IntegralDomain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class BigRational implements Comparable<BigRational>, Field<BigRational> {

    public final static BigRational ZERO = new BigRational(0);
    public final static BigRational ONE  = new BigRational(1);
    public final static BigRational TWO  = new BigRational(2);
    public final static BigRational NONE  = new BigRational(-1);
    
    private BigInteger num;   // the numerator
    private BigInteger den;   // the denominator (always a positive integer)


    // create and initialize a new BigRational object
    public BigRational(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    // create and initialize a new BigRational object
    public BigRational(int numerator) {
        this(numerator, 1);
    }

    // create and initialize a new BigRational object from a string, e.g., "-343/1273"
    public BigRational(String s) {
        String[] tokens = s.split("/");
        if (tokens.length == 2)
            init(new BigInteger(tokens[0]), new BigInteger(tokens[1]));
        else if (tokens.length == 1)
            init(new BigInteger(tokens[0]), BigInteger.ONE);
        else
            throw new IllegalArgumentException("For input string: \"" + s + "\"");
    }

    // create and initialize a new BigRational object
    public BigRational(BigInteger numerator, BigInteger denominator) {
        init(numerator, denominator);
    }

    private void init(BigInteger numerator, BigInteger denominator) {

        // deal with x / 0
        if (denominator.equals(BigInteger.ZERO)) {
           throw new ArithmeticException("Denominator is zero");
        }

        // reduce fraction (if num = 0, will always yield den = 0)
        BigInteger g = numerator.gcd(denominator);
        num = numerator.divide(g);
        den = denominator.divide(g);

        // to ensure invariant that denominator is positive
        if (den.compareTo(BigInteger.ZERO) < 0) {
            den = den.negate();
            num = num.negate();
        }
    }

    // return string representation of (this)
    public String toString() { 
        if (den.equals(BigInteger.ONE)) return num + "";
        else                            return num + "/" + den;
    }

    // return { -1, 0, + 1 } if a < b, a = b, or a > b
    public int compareTo(BigRational b) {
        BigRational a = this;
        return a.num.multiply(b.den).compareTo(a.den.multiply(b.num));
    }

    // is this BigRational negative, zero, or positive?
    public boolean isZero()     { return num.signum() == 0; }
    public boolean isPositive() { return num.signum() >  0; }
    public boolean isNegative() { return num.signum() <  0; }

    // is this Rational object equal to y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;  
        if (y.getClass() != this.getClass()) return false;
        BigRational b = (BigRational) y;
        return compareTo(b) == 0;
    }
        
    // hashCode consistent with equals() and compareTo()
    public int hashCode() {
        return Objects.hash(num, den);
    }
    

    // return a * b
    public BigRational times(BigRational b) {
        BigRational a = this;
        return new BigRational(a.num.multiply(b.num), a.den.multiply(b.den));
    }

    // return a + b
    public BigRational plus(BigRational b) {
        BigRational a = this;
        BigInteger numerator   = a.num.multiply(b.den).add(b.num.multiply(a.den));
        BigInteger denominator = a.den.multiply(b.den);
        return new BigRational(numerator, denominator);
    }

    // return -a
    public BigRational negate() {
        return new BigRational(num.negate(), den);
    }

    // return |a|
    public BigRational abs() {
        if (isNegative()) return negate();
        else return this;
    }

    // return a - b
    public BigRational minus(BigRational b) {
        BigRational a = this;
        return a.plus(b.negate());
    }

    // return 1 / a
    public BigRational reciprocal() {
        return new BigRational(den, num);
    }

    // return a / b
    public BigRational divides(BigRational b) {
        BigRational a = this;
        return a.times(b.reciprocal());
    }

    // return double reprentation (within given precision)
    public double doubleValue() {
        int SCALE = 32;        // number of digits after the decimal place
        BigDecimal numerator   = new BigDecimal(num);
        BigDecimal denominator = new BigDecimal(den);
        BigDecimal quotient    = numerator.divide(denominator, SCALE, RoundingMode.HALF_EVEN);
        return quotient.doubleValue();
    }

    @Override
    public BigRational ZERO()
    {
	return ZERO;
    }

    @Override
    public BigRational ONE()
    {
	return ONE;
    }

    @Override
    public BigRational NONE()
    {
	return NONE;
    }

    @Override
    public BigRational add(BigRational b)
    {
	return this.plus(b);
    }

    @Override
    public BigRational sub(BigRational b)
    {
	return this.minus(b);
    }

    @Override
    public BigRational mul(BigRational b)
    {
	return this.times(b);
    }

    @Override
    public BigRational div(BigRational b)
    {
	return this.divides(b);
    }

    @Override
    public BigRational invert()
    {
	return this.reciprocal();
    }

    @Override
    public BigRational pow(long p)
    {
	if(p == 0)
	    return ONE;
	if(p < 0)
	    return this.reciprocal().pow(-p);
	if(p % 2 == 0)
	    return this.times(this).pow(p/2);
	return this.times(this.pow(p-1));
    }

    @Override
    public boolean isOne()
    {
	return this.equals(ONE);
    }

    
}