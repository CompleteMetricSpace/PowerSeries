package interfaces;

public interface IntegralDomain<F extends IntegralDomain<F>> 
{
    public F NONE();
    public F ZERO();
    public F ONE();
    public F add(F b);
    public F sub(F b);
    public F mul(F b);
    public F negate();
    public boolean isZero();
    public boolean isOne();
    public boolean equals(Object b);
}
