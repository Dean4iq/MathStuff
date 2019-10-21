package ua.den.service.primitive;

import java.math.BigDecimal;

public class AddFunction implements PrimitiveFunction {
    @Override
    public BigDecimal solve(BigDecimal x, BigDecimal y) {
        return x.add(y);
    }
}
