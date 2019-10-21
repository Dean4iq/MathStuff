package ua.den.service;

import ua.den.exceptions.DivisibleIsLessThanDividerException;
import ua.den.exceptions.DivisionOnNullException;

import java.math.BigInteger;

public class BasicMathStuff {
    public String add(String a, String b) {
        boolean aIsNegative = a.contains("-");
        boolean bIsNegative = b.contains("-");

        if (aIsNegative && bIsNegative) {
            a = a.substring(1);
            b = b.substring(1);

            String result = performAdd(a, b);
            return "-" + result;
        } else if (!aIsNegative && bIsNegative) {
            b = b.substring(1);
            return subtract(a, b);
        } else if (aIsNegative) {
            a = a.substring(1);
            return subtract(b, a);
        }

        return performAdd(a, b);
    }

    public String subtract(String a, String b) {
        boolean aIsNegative = a.contains("-");
        boolean bIsNegative = b.contains("-");

        if (aIsNegative && !bIsNegative) {
            b = "-" + b;
            return add(a, b);
        } else if (bIsNegative) {
            b = b.substring(1);
            return add(a, b);
        }

        return performSubtract(a, b);
    }

    public String multiply(String a, String b) {
        boolean aIsNegative = a.contains("-");
        boolean bIsNegative = b.contains("-");

        if (aIsNegative) {
            a = a.substring(1);
        }
        if (bIsNegative) {
            b = b.substring(1);
        }

        String result = performMultiply(a, b);

        if ((!aIsNegative && bIsNegative) || (aIsNegative && !bIsNegative)) {
            result = "-" + result;
        }

        return result;
    }

    public String divide(String a, String b) {
        boolean aIsNegative = a.contains("-");
        boolean bIsNegative = b.contains("-");

        if (aIsNegative) {
            a = a.substring(1);
        }
        if (bIsNegative) {
            b = b.substring(1);
        }

        if (new BigInteger(b).equals(new BigInteger("0"))) {
            throw new DivisionOnNullException();
        }
        if (new BigInteger(a, 2).equals(new BigInteger("0"))) {
            return "0";
        }
        if (new BigInteger(a, 2).compareTo(new BigInteger(b, 2)) < 0) {
            throw new DivisibleIsLessThanDividerException(a, b);
        }

        String result = performDivide(a, b);

        if ((!aIsNegative && bIsNegative) || (aIsNegative && !bIsNegative)) {
            result = "-" + result;
        }

        return result;
    }

    private String performAdd(String a, String b) {
        if (a.length() != b.length()) {
            String[] equalSizedValues = fillDifferenceWithZero(a, b);
            a = equalSizedValues[0];
            b = equalSizedValues[1];
        }

        char[] aNum = a.toCharArray();
        char[] bNum = b.toCharArray();
        char[] resultNum = new char[Math.max(aNum.length, bNum.length)];
        boolean overFloated = false;

        for (int i = resultNum.length - 1; i >= 0; i--) {
            resultNum[i] = sumBinaryDigits(aNum[i], bNum[i]);

            if (overFloated) {
                resultNum[i] = sumBinaryDigits(resultNum[i], '1');
                overFloated = false;
            }

            if (resultNum[i] == '2') {
                resultNum[i] = '0';
                overFloated = true;
            } else if (resultNum[i] == '3') {
                resultNum[i] = '1';
                overFloated = true;
            }
        }

        String result = new String(resultNum);
        if (overFloated) {
            result = '1' + result;
        }

        return result;
    }

    private String performSubtract(String a, String b) {
        if (a.length() != b.length()) {
            String[] equalSizedValues = fillDifferenceWithZero(a, b);
            a = equalSizedValues[0];
            b = equalSizedValues[1];
        }

        char[] aNum = a.toCharArray();
        char[] bNum = b.toCharArray();
        char[] resultNum = new char[Math.max(aNum.length, bNum.length)];
        boolean overSubtracted = false;

        for (int i = resultNum.length - 1; i >= 0; i--) {
            resultNum[i] = subtractBinaryDigits(aNum[i], bNum[i]);

            if (overSubtracted) {
                resultNum[i] = subtractBinaryDigits(resultNum[i], '1');
                overSubtracted = false;
            }

            if (resultNum[i] == '2') {
                resultNum[i] = '1';
                overSubtracted = true;
            } else if (resultNum[i] == '3') {
                resultNum[i] = '0';
                overSubtracted = true;
            }
        }

        String result = new String(resultNum);

        if (overSubtracted) {
            result = "-" + invertForSubtraction(result);
        }

        return result;
    }

    private String performMultiply(String a, String b) {
        char[] aNum = a.toCharArray();
        char[] bNum = b.toCharArray();
        char[] resultNumForIteration = new char[aNum.length];
        String result = "";

        for (int i = bNum.length - 1; i >= 0; i--) {
            for (int j = aNum.length - 1; j >= 0; j--) {
                resultNumForIteration[j] = multiplyBinaryDigits(aNum[j], bNum[i]);
            }

            result = add(result, addZerosToMultiplyIteration(new String(resultNumForIteration), bNum.length - 1 - i));
        }

        return result;
    }

    private String performDivide(String a, String b) {
        char[] aNum = a.toCharArray();
        char[] bNum = b.toCharArray();
        StringBuilder result = new StringBuilder();
        int idx = bNum.length;
        int repeatedWithoutOperating = 0;

        while (idx <= a.length()) {
            char[] aBuffer = new String(aNum).substring(0, idx).toCharArray();

            String subtractOperandsResult = performSubtract(new String(aBuffer), b);

            if (subtractOperandsResult.contains("-")) {
                repeatedWithoutOperating++;
            } else {
                char[] subtractOperandsResultChar = subtractOperandsResult.toCharArray();

                System.arraycopy(subtractOperandsResultChar, 0, aNum, 0, subtractOperandsResultChar.length);

                for (int i = 0; i < repeatedWithoutOperating && result.length() != 0; i++) {
                    result.append("0");
                }

                repeatedWithoutOperating = 0;
                result.append("1");
            }

            idx++;
        }

        if (repeatedWithoutOperating > 0) {
            for (int i = 0; i < repeatedWithoutOperating; i++) {
                result.append("0");
            }
        }

        return result.toString();
    }

    private String[] fillDifferenceWithZero(String a, String b) {
        if (a.length() < b.length()) {
            StringBuilder aBuilder = new StringBuilder(a);

            while (aBuilder.length() < b.length()) {
                aBuilder.insert(0, '0');
            }

            a = aBuilder.toString();
        } else {
            StringBuilder bBuilder = new StringBuilder(b);

            while (bBuilder.length() < a.length()) {
                bBuilder.insert(0, '0');
            }

            b = bBuilder.toString();
        }

        return new String[]{a, b};
    }

    private char sumBinaryDigits(char a, char b) {
        if (a == '0' && b == '0') {
            return '0';
        } else if (a == '1' && b == '0' || a == '0' && b == '1') {
            return '1';
        } else if (a == '1' && b == '1') {
            return '2';
        } else {
            return '3';
        }
    }

    private char subtractBinaryDigits(char a, char b) {
        if (a == '0' && b == '0' || a == '1' && b == '1') {
            return '0';
        } else if (a == '0' && b == '1') {
            return '2';
        } else if (a == '1' && b == '0') {
            return '1';
        } else {
            return '3';
        }
    }

    private char multiplyBinaryDigits(char a, char b) {
        return (a == '0' || b == '0') ? '0' : '1';
    }

    private String invertForSubtraction(String number) {
        char[] result = number.toCharArray();

        for (int i = 0; i < result.length; i++) {
            result[i] = (result[i] == '1') ? '0' : '1';
        }

        return add(new String(result), "1");
    }

    private String addZerosToMultiplyIteration(String number, int iteration) {
        StringBuilder zeros = new StringBuilder(number);

        for (int i = 0; i < iteration; i++) {
            zeros.append("0");
        }

        return zeros.toString();
    }
}
