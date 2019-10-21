package ua.den.service.symbols;

import ua.den.service.primitive.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FunctionSymbols {
    public static final String OPEN_DELIMITER = "(";
    public static final String CLOSE_DELIMITER = ")";
    public static final char OPEN_DELIMITER_CHAR = '(';
    public static final char CLOSE_DELIMITER_CHAR = ')';
    public static Map<String, PrimitiveFunction> mathCharacters = new HashMap<>();
    public static Map<String, PrimitiveFunction> operationCharacters = new HashMap<>();
    public static Set<String> operandsToDelimit = new HashSet<>();

    static {
        mathCharacters.put("*", new MultiplyFunction());
        mathCharacters.put("/", new DivideFunction());
        mathCharacters.put("+", new AddFunction());
        mathCharacters.put("-", new SubtractFunction());

        operationCharacters.put("^", new PowFunction());

        operandsToDelimit.add("*");
        operandsToDelimit.add("/");
        operandsToDelimit.add("^");
    }
}
