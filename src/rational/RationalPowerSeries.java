package rational;

import polynomial.Polynomial;
import polynomial.PolynomialDivision;
import interfaces.Field;
import series.ExtendedPowerSeries;
import series.PowerSeries;

public class RationalPowerSeries
{
    public static <F extends Field<F>> ExtendedPowerSeries<F> rationalFunctionToExtendedPowerSeries(
	    RationalFunction<F> f, F a)
    {
	Polynomial<F> numerator = PolynomialDivision.expandInAPoint(f.getNumerator(), a);
	Polynomial<F> denominator = PolynomialDivision.expandInAPoint(f.getDenominator(), a);
	return new ExtendedPowerSeries<F>(new PowerSeries<F>(numerator))
		.div(new ExtendedPowerSeries<F>(new PowerSeries<F>(denominator)));
    }

}
