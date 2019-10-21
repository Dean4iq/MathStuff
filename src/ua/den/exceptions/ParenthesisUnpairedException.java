package ua.den.exceptions;

public class ParenthesisUnpairedException extends IllegalArgumentException {
    public ParenthesisUnpairedException() {
        super("Unpaired parenthesis found!");
    }
}
