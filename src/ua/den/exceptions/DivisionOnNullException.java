package ua.den.exceptions;

public class DivisionOnNullException extends IllegalArgumentException {
    public DivisionOnNullException() {
        super("Shouldn't be divided by zero");
    }
}
