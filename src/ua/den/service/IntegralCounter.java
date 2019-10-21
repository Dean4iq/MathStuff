package ua.den.service;

import java.math.BigDecimal;
import java.util.HashMap;

public class IntegralCounter {
    private String function;
    private double x1;
    private double x2;
    private long intervalsCount;
    private int scale;

    public BigDecimal solve() {
        BigDecimal result = BigDecimal.ZERO;
        double intervals = (x2 - x1) / intervalsCount;
        BigDecimal intervalsObject = new BigDecimal(intervals).setScale(scale, BigDecimal.ROUND_HALF_UP);

        for (double i = x1; i <= x2; i += intervals) {
            double finalI = i;

            FunctionAnalyzer functionAnalyzer = new FunctionAnalyzer(new HashMap<String, BigDecimal>() {{
                put("x", new BigDecimal(finalI));
                put("y", new BigDecimal(2));
            }});

            functionAnalyzer.setFunction(function);
            functionAnalyzer.setRoundingMethod(BigDecimal.ROUND_HALF_UP);
            functionAnalyzer.setScale(scale);

            result = result.add(functionAnalyzer.solve().setScale(scale, BigDecimal.ROUND_HALF_UP)).setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        
        return result.
                multiply(intervalsObject).
                setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public long getIntervalsCount() {
        return intervalsCount;
    }

    public void setIntervalsCount(long intervalsCount) {
        this.intervalsCount = intervalsCount;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
