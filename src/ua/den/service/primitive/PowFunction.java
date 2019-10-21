package ua.den.service.primitive;

import java.math.BigDecimal;

public class PowFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return x.pow(y.intValue());
    }
}
