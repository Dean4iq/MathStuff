package ua.den.service.primitive;

import java.math.BigDecimal;

@FunctionalInterface
public interface PrimitiveFunction {
    BigDecimal solve(BigDecimal x, BigDecimal y);
}
