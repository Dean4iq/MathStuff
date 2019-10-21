package ua.den.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static ua.den.service.symbols.FunctionSymbols.*;

public class FunctionSolver {
    private Map<String, BigDecimal> variables;
    private FunctionAnalyzer functionAnalyzer;

    public BigDecimal solve(FunctionAnalyzer functionAnalyzer) {
        this.functionAnalyzer = functionAnalyzer;
        variables = functionAnalyzer.getVariables();

        return interpretFunction(functionAnalyzer.getFunction().toCharArray(), BigDecimal.ZERO);
    }

    private BigDecimal interpretFunction(char[] function, BigDecimal result) {
        StringBuilder iterationSymbol = new StringBuilder();
        int functionIndex = function.length - 1;

        while (functionIndex >= 0) {
            iterationSymbol.append(function[functionIndex]);
            String iterationSymbolString = iterationSymbol.toString();

            if (mathCharacters.containsKey(iterationSymbolString)
                    || operationCharacters.containsKey(iterationSymbolString)
                    || variables.containsKey(iterationSymbolString)
                    || OPEN_DELIMITER.equals(iterationSymbolString)
                    || CLOSE_DELIMITER.equals(iterationSymbolString)
                    || iterationSymbolString.matches("(\\d|\\.)+")) {
                if (mathCharacters.containsKey(iterationSymbolString)) {
                    result = mathCharacters.get(iterationSymbolString).solve(interpretFunction(Arrays.copyOfRange(function, 0, functionIndex), BigDecimal.ZERO), result);
                    break;
                } else if (variables.containsKey(iterationSymbolString)) {
                    result = variables.get(iterationSymbolString);
                } else if (operationCharacters.containsKey(iterationSymbolString)) {
                    result = operationCharacters.get(iterationSymbolString).solve(interpretFunction(Arrays.copyOfRange(function, 0, functionIndex), BigDecimal.ZERO), result);
                    break;
                } else if (iterationSymbolString.matches("(\\d|\\.)+")) {
                    if (functionIndex > 0) {
                        if (Character.toString(function[functionIndex - 1]).matches("[^\\.\\d]")) {
                            iterationSymbolString = iterationSymbol.reverse().toString();
                            result = new BigDecimal(iterationSymbolString);
                        } else {
                            functionIndex--;
                            continue;
                        }
                    } else {
                        iterationSymbolString = iterationSymbol.reverse().toString();
                        result = new BigDecimal(iterationSymbolString);
                    }
                } else {
                    result = resolveDelimitedPart(Arrays.copyOfRange(function, 0, functionIndex));
                    function = cutDelimitedPart(function);
                    functionIndex = function.length;
                }

                iterationSymbol.setLength(0);
            }

            functionIndex--;
        }

        return result.setScale(functionAnalyzer.getScale(), functionAnalyzer.getRoundingMethod());
    }

    private BigDecimal resolveDelimitedPart(char[] function) {
        BigDecimal buffer = BigDecimal.ZERO;
        String ss = new String(function);
        int index = function.length - 1;
        while (index >= 0) {
            if (function[index] == OPEN_DELIMITER_CHAR) {
                return interpretFunction(Arrays.copyOfRange(function, index + 1, function.length), buffer);
            } else if (function[index] == CLOSE_DELIMITER_CHAR) {
                buffer = resolveDelimitedPart(Arrays.copyOfRange(function, 0, index));

                int closedQ = 0;
                for (int i = index - 1; i >= 0; i--) {
                    if (function[i] == ')') {
                        closedQ++;
                    }
                    if (function[i] == '(') {
                        if (closedQ == 0) {
                            index = i;
                            break;
                        } else {
                            closedQ--;
                        }
                    }
                }
            }

            index--;
        }

        return buffer;
    }

    private char[] cutDelimitedPart(char[] function) {
        int countOfCloseDelimiter = 0;

        for (int i = function.length - 1; i >= 0; i--) {
            if (function[i] == CLOSE_DELIMITER_CHAR) {
                countOfCloseDelimiter++;
            } else if (function[i] == OPEN_DELIMITER_CHAR) {
                countOfCloseDelimiter--;
                if (countOfCloseDelimiter == 0) {
                    return Arrays.copyOfRange(function, 0, i);
                }
            }
        }

        throw new RuntimeException();
    }
}
