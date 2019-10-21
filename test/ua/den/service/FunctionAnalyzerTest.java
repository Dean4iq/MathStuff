package ua.den.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class FunctionAnalyzerTest {
    private FunctionAnalyzer testedObject;
    private Map<String, BigDecimal> variables;

    private double x = 45;
    private double y = 22;
    private double z = 100;
    private int roundingMethod = BigDecimal.ROUND_HALF_UP;
    private int scale = 11;
    private String function = "100+30^2/x-z+y*(x+123-0.001)";

    @Before
    public void setUp() {
        variables = new HashMap<>();
        testedObject = new FunctionAnalyzer(variables);

        variables.put("x", new BigDecimal(x));
        variables.put("y", new BigDecimal(y));
        variables.put("z", new BigDecimal(z));

        testedObject.setScale(scale);
        testedObject.setRoundingMethod(roundingMethod);
        testedObject.setFunction(function);
    }

    @Test
    public void solve() {
        assertEquals(solveFunc(), testedObject.solve());
    }

    @Test
    public void checkPointedNumber() {
        function = "x+0.1";
        testedObject.setFunction(function);

        assertEquals(new BigDecimal(x + 0.1).setScale(scale, roundingMethod), testedObject.solve());
    }

    @Test
    public void checkPower() {
        function = "3^(3^3)";
        testedObject.setFunction(function);

        assertEquals(new BigDecimal(Math.pow(3, Math.pow(3, 3))).setScale(scale, roundingMethod), testedObject.solve());
    }

    private BigDecimal solveFunc() {
        BigDecimal result = BigDecimal.ZERO.setScale(scale, roundingMethod);

        result = result.add(new BigDecimal(100).setScale(scale, roundingMethod)).setScale(scale, roundingMethod);
        result = result.add(new BigDecimal(y).setScale(scale, roundingMethod).multiply(new BigDecimal(x).setScale(scale, roundingMethod).add(new BigDecimal(123).setScale(scale, roundingMethod)).subtract(new BigDecimal("0.001").setScale(scale, roundingMethod)).setScale(scale, roundingMethod))).setScale(scale, roundingMethod);
        result = result.subtract(new BigDecimal(z).setScale(scale, roundingMethod)).setScale(scale, roundingMethod);
        result = result.add(new BigDecimal(30).setScale(scale, roundingMethod).pow(2).setScale(scale, roundingMethod).divide(new BigDecimal(x).setScale(scale, roundingMethod), BigDecimal.ROUND_HALF_UP).setScale(scale, roundingMethod)).setScale(scale, roundingMethod);

        return result.setScale(scale, roundingMethod);
    }
}