package ua.den.service.primitive;

import java.math.BigDecimal;

public class DivideFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return x.divide(y, BigDecimal.ROUND_HALF_UP);
    }
}
