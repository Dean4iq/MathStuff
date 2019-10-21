package ua.den.service.primitive;

import java.math.BigDecimal;

public class SubtractFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return x.subtract(y);
    }
}
