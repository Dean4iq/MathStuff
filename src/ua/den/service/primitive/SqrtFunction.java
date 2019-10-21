package ua.den.service.primitive;

import java.math.BigDecimal;

public class SqrtFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return new BigDecimal(Math.sqrt(y.doubleValue()));
    }
}
