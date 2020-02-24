package ua.den.service.primitive;

import java.math.BigDecimal;

public class SqrtFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return BigDecimal.valueOf(Math.sqrt(y.doubleValue()));
    }
}
