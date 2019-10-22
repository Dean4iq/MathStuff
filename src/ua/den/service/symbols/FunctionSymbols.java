package ua.den.service.symbols;

import ua.den.service.primitive.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FunctionSymbols {
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = ")";
    public static final char OPEN_PARENTHESIS_CHAR = '(';
    public static final char CLOSE_PARENTHESIS_CHAR = ')';
    public static Map<String, PrimitiveFunction> mathCharacters = new HashMap<>();
    public static Map<String, PrimitiveFunction> operationCharacters = new HashMap<>();
    public static Set<String> operandsToParenthesise = new HashSet<>();

    static {
        mathCharacters.put("*", new MultiplyFunction());
        mathCharacters.put("/", new DivideFunction());
        mathCharacters.put("+", new AddFunction());
        mathCharacters.put("-", new SubtractFunction());

        operationCharacters.put("^", new PowFunction());
        operationCharacters.put("sqrt", new SqrtFunction());

        operandsToParenthesise.add("*");
        operandsToParenthesise.add("/");
        operandsToParenthesise.add("^");
        operandsToParenthesise.add("sqrt");
    }
}
