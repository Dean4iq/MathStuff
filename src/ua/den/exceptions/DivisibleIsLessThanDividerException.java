package ua.den.exceptions;

public class DivisibleIsLessThanDividerException extends IllegalArgumentException {
    public DivisibleIsLessThanDividerException(String divisible, String divider) {
        super("Divisible can't be less than divider: '" + divisible + "' vs '" + divider + "'");
    }
}
