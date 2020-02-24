package ua.den.service.primitive;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DivideFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return x.divide(y, RoundingMode.HALF_UP);
    }
}
