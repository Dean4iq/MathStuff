package ua.den.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import ua.den.exceptions.DivisibleIsLessThanDividerException;
import ua.den.exceptions.DivisionOnNullException;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class BasicMathStuffTest {
    private BasicMathStuff testedObject;

    private String numberABinary;
    private String negativeNumberABinary;
    private String numberBBinary;
    private String negativeNumberBBinary;

    private BigInteger a;
    private BigInteger b;

    @Before
    public void setUp() {
        testedObject = new BasicMathStuff();

        numberABinary = "110011";
        numberBBinary = "1111011";
        negativeNumberABinary = "-11101101";
        negativeNumberBBinary = "-11101";
    }

    @Test
    public void addBothPositive() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.add(b), new BigInteger(testedObject.add(numberABinary, numberBBinary),2));
    }

    @Test
    public void addBothNegative() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.add(b), new BigInteger(testedObject.add(negativeNumberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void addMixPositiveAndNegative() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.add(b), new BigInteger(testedObject.add(numberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void addMixNegativeAndPositive() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.add(b), new BigInteger(testedObject.add(negativeNumberABinary, numberBBinary),2));
    }

    @Test
    public void subtractBothPositive() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.subtract(b), new BigInteger(testedObject.subtract(numberABinary, numberBBinary),2));
    }

    @Test
    public void subtractBothNegative() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.subtract(b), new BigInteger(testedObject.subtract(negativeNumberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void subtractMixPositiveAndNegative() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.subtract(b), new BigInteger(testedObject.subtract(numberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void subtractMixNegativeAndPositive() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.subtract(b), new BigInteger(testedObject.subtract(negativeNumberABinary, numberBBinary),2));
    }

    @Test
    public void multiplyBothPositive() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.multiply(b), new BigInteger(testedObject.multiply(numberABinary, numberBBinary),2));
    }

    @Test
    public void multiplyBothNegative() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.multiply(b), new BigInteger(testedObject.multiply(negativeNumberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void multiplyMixPositiveAndNegative() {
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(negativeNumberBBinary,2);

        assertEquals(a.multiply(b), new BigInteger(testedObject.multiply(numberABinary, negativeNumberBBinary),2));
    }

    @Test
    public void multiplyMixNegativeAndPositive() {
        a = new BigInteger(negativeNumberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.multiply(b), new BigInteger(testedObject.multiply(negativeNumberABinary, numberBBinary),2));
    }

    @Test(expected = DivisibleIsLessThanDividerException.class)
    public void shouldNotDivide() {
        numberABinary = "1010";
        numberBBinary = "1111";

        testedObject.divide(numberABinary, numberBBinary);
    }

    @Test(expected = DivisionOnNullException.class)
    public void divisionByZero() {
        numberABinary = "1010";
        numberBBinary = "0";

        System.out.println(testedObject.divide(numberABinary, numberBBinary));
    }

    @Test
    public void divisionForZero() {
        numberABinary = "0";
        numberBBinary = "1111";

        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.divide(b), new BigInteger(testedObject.divide(numberABinary, numberBBinary)));
    }

    @Test
    public void divideBothPositive() {
        numberABinary = "1011110100";
        numberBBinary = "1110";
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.divide(b), new BigInteger(testedObject.divide(numberABinary, numberBBinary),2));
    }

    @Test
    public void divideBothNegative() {
        numberABinary = "-1010";
        numberBBinary = "-101";
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.divide(b), new BigInteger(testedObject.divide(numberABinary, numberBBinary),2));
    }

    @Test
    public void divideMixPositiveAndNegative() {
        numberABinary = "1010";
        numberBBinary = "-11";
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.divide(b), new BigInteger(testedObject.divide(numberABinary, numberBBinary),2));
    }

    @Test
    public void divideMixNegativeAndPositive() {
        numberABinary = "-1010";
        numberBBinary = "11";
        a = new BigInteger(numberABinary,2);
        b = new BigInteger(numberBBinary,2);

        assertEquals(a.divide(b), new BigInteger(testedObject.divide(numberABinary, numberBBinary),2));
    }
}