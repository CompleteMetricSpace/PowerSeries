package series;

import interfaces.Field;

public class ExtendedPowerSeries<F extends Field<F>> implements Field<ExtendedPowerSeries<F>>
{
    private PowerSeries<F> mainSeries;
    private Long power;
    F obj;

    public ExtendedPowerSeries(PowerSeries<F> series, Long order)
    {
	this(series, order, series.obj);
    }

    public ExtendedPowerSeries(PowerSeries<F> series, Long order, F obj)
    {
	mainSeries = series;
	power = order;
	this.obj = obj;
	this.normalize();
    }
    
    public ExtendedPowerSeries(PowerSeries<F> series)
    {
	this(series, 0L);
    }

    public PowerSeries<F> getMainSeries()
    {
	return mainSeries;
    }

    public Long getOrder()
    {
	return power;
    }

    private void normalize()
    {
	Long ord = mainSeries.order();
	if(ord == null)
	{
	    power = 0L;
	}
	else
	{
	    mainSeries = mainSeries.shift(-ord);
	    power = power - ord;
	}
    }

    @Override
    public ExtendedPowerSeries<F> NONE()
    {
	return new ExtendedPowerSeries<F>(mainSeries.NONE(), 0L, obj);
    }

    @Override
    public ExtendedPowerSeries<F> ZERO()
    {
	return new ExtendedPowerSeries<F>(mainSeries.ZERO(), 0L, obj);
    }

    @Override
    public ExtendedPowerSeries<F> ONE()
    {
	return new ExtendedPowerSeries<F>(mainSeries.ONE(), 0L, obj);
    }

    @Override
    public ExtendedPowerSeries<F> add(ExtendedPowerSeries<F> b)
    {
	Long m = getOrder(), n = b.getOrder();
	Long u = Math.max(m, n);
	return new ExtendedPowerSeries<F>(getMainSeries().shift(u - m).add(
		b.getMainSeries().shift(u - n)), u, obj);
    }

    @Override
    public ExtendedPowerSeries<F> sub(ExtendedPowerSeries<F> b)
    {
	Long m = getOrder(), n = b.getOrder();
	Long u = Math.max(m, n);
	return new ExtendedPowerSeries<F>(getMainSeries().shift(u - m).sub(
		b.getMainSeries().shift(u - n)), u, obj);
    }

    @Override
    public ExtendedPowerSeries<F> mul(ExtendedPowerSeries<F> b)
    {
	return new ExtendedPowerSeries<F>(getMainSeries().mul(b.getMainSeries()), getOrder()+b.getOrder(), obj);
    }

    @Override
    public ExtendedPowerSeries<F> negate()
    {
	return new ExtendedPowerSeries<F>(getMainSeries().negate(), getOrder(), obj);
    }

    @Override
    public boolean isZero()
    {
	return mainSeries.isZero();
    }

    @Override
    public boolean isOne()
    {
	return mainSeries.isOne();
    }

    @Override
    public ExtendedPowerSeries<F> div(ExtendedPowerSeries<F> b)
    {
	return this.mul(b.invert());
    }

    @Override
    public ExtendedPowerSeries<F> invert()
    {
	return new ExtendedPowerSeries<F>(mainSeries.invert(), -power, obj);
    }

    @Override
    public ExtendedPowerSeries<F> pow(long p)
    {
	if(p == 0)
	    return ONE();
	if(p < 0)
	    return this.invert().pow(-p);
	if(p % 2 == 0)
	    return this.mul(this).pow(p / 2);
	return this.mul(this.pow(p - 1));
    }

}
