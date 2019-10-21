package ua.den.exceptions;

public class DelimiterUnpairedException extends IllegalArgumentException {
    public DelimiterUnpairedException() {
        super("Unpaired delimiters found!");
    }
}
