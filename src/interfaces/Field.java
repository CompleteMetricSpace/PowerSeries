package interfaces;

public interface Field<F extends Field<F>> extends IntegralDomain<F>
{   
    public F div(F b);
    public F invert();
    public F pow(long p);
}
