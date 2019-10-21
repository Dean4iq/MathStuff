package ua.den.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(BlockJUnit4ClassRunner.class)
public class IntegralCounterTest {
    private IntegralCounter testedObject;
    private String function;
    private int x1;
    private int x2;

    @Before
    public void setUp() {
        testedObject = new IntegralCounter();
        function = "2*x+101+0.1";
        x1 = 0;
        x2 = 10;

        testedObject.setX1(x1);
        testedObject.setX2(x2);
        testedObject.setFunction(function);
        testedObject.setScale(15);
        testedObject.setIntervalsCount(10000000L);
    }

    @Test
    public void solve() {
        assertNotNull(testedObject.solve());
    }
}