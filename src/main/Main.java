package main;

import java.util.HashMap;
import java.util.Map;

import algebraic.AlgebraicNumber;
import polynomial.Polynomial;
import polynomial.PolynomialDivision;
import polynomial.PolynomialGCD;
import rational.RationalFunction;
import rational.RationalPowerSeries;
import series.ExtendedPowerSeries;
import misc.Pair;
import misc.QuotientField;
import number.BigRational;

public class Main
{
    public static void main(String[] args)
    {
	Polynomial<BigRational> p1 = new Polynomial<>(BigRational.ZERO,
		new BigRational[] {
			new BigRational(-2, 1),
			new BigRational(0, 1),
			new BigRational(1, 1)
		});
	Polynomial<BigRational> q1 = new Polynomial<>(BigRational.ZERO,
		new BigRational[] {
			new BigRational(2, 1),
			new BigRational(1, 1),
			new BigRational(-4, 1),
			new BigRational(1, 1)
		});
	Polynomial<BigRational> p2 = new Polynomial<>(BigRational.ZERO,
		new BigRational[] {
			new BigRational(1, 1),
			new BigRational(1, 1)
		});
	Polynomial<BigRational> q2 = new Polynomial<>(BigRational.ZERO,
		new BigRational[] {
			new BigRational(1, 1),
		});
	RationalFunction<BigRational> r1 = new RationalFunction<>(p1, q1);
	RationalFunction<BigRational> r2 = new RationalFunction<>(p2, q2);
	ExtendedPowerSeries<BigRational> eps1 = RationalPowerSeries.rationalFunctionToExtendedPowerSeries(r1, new BigRational(1,1));
	//System.out.println(p1.sub(q1));
	AlgebraicNumber<BigRational> num = new AlgebraicNumber<BigRational>(p2, p1);
	System.out.println("s: "+num.invert().mul(num));
    }
}
