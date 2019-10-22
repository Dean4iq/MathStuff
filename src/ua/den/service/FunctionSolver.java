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
            String f = new String(function);
            iterationSymbol.append(function[functionIndex]);
            String iterationSymbolString = new StringBuilder(iterationSymbol.toString()).reverse().toString();

            if (mathCharacters.containsKey(iterationSymbolString)
                    || operationCharacters.containsKey(iterationSymbolString)
                    || variables.containsKey(iterationSymbolString)
                    || OPEN_PARENTHESIS.equals(iterationSymbolString)
                    || CLOSE_PARENTHESIS.equals(iterationSymbolString)
                    || iterationSymbolString.matches("(\\d|\\.)+")) {
                if (mathCharacters.containsKey(iterationSymbolString)) {
                    result = mathCharacters.get(iterationSymbolString).solve(interpretFunction(Arrays.copyOfRange(function, 0, functionIndex), BigDecimal.ZERO), result);
                    break;
                } else if (variables.containsKey(iterationSymbolString)) {
                    result = variables.get(iterationSymbolString);
                } else if (operationCharacters.containsKey(iterationSymbolString)) {
                    result = operationCharacters.get(iterationSymbolString).solve(interpretFunction(Arrays.copyOfRange(function, 0, functionIndex), BigDecimal.ZERO), result);
                    break;
                } else if (iterationSymbolString.matches("(\\d|\\.|-)+")) {
                    if (functionIndex > 0) {
                        if (Character.toString(function[functionIndex - 1]).matches("[^\\.\\d-]")) {
                            result = new BigDecimal(iterationSymbolString);
                        } else {
                            if (Character.toString(function[functionIndex - 1]).matches("[-]")) {
                                if (functionIndex > 1) {
                                    StringBuilder buffer = new StringBuilder();
                                    for (int i = functionIndex - 2; i >= 0; i--) {
                                        buffer.append(function[i]);
                                        String bufferedString = buffer.toString();
                                        if (mathCharacters.containsKey(bufferedString)
                                                || operationCharacters.containsKey(bufferedString)
                                                || OPEN_PARENTHESIS.equals(bufferedString)) {
                                            iterationSymbol.append(functionIndex - 1);
                                            iterationSymbolString = iterationSymbol.reverse().toString();
                                            result = new BigDecimal(iterationSymbolString);
                                            functionIndex--;
                                            break;
                                        } else if (variables.containsKey(bufferedString)
                                                || CLOSE_PARENTHESIS.equals(bufferedString)
                                                || bufferedString.matches("(\\d|\\.)+")) {
                                            result = new BigDecimal(iterationSymbolString);
                                            break;
                                        }
                                    }
                                } else {
                                    iterationSymbol.append(functionIndex - 1);
                                    iterationSymbolString = iterationSymbol.reverse().toString();
                                    result = new BigDecimal(iterationSymbolString);
                                    functionIndex--;
                                }
                            } else {
                                functionIndex--;
                                continue;
                            }
                        }
                    } else {
                        result = new BigDecimal(iterationSymbolString);
                    }
                } else {
                    result = resolveParentesisedPart(Arrays.copyOfRange(function, 0, functionIndex));
                    function = cutParentesisedPart(function);
                    functionIndex = function.length;
                }

                iterationSymbol.setLength(0);
            }

            functionIndex--;
        }

        return result.setScale(functionAnalyzer.getScale(), functionAnalyzer.getRoundingMethod());
    }

    private BigDecimal resolveParentesisedPart(char[] function) {
        BigDecimal buffer = BigDecimal.ZERO;

        int index = function.length - 1;
        while (index >= 0) {
            if (function[index] == OPEN_PARENTHESIS_CHAR) {
                return interpretFunction(Arrays.copyOfRange(function, index + 1, function.length), buffer);
            } else if (function[index] == CLOSE_PARENTHESIS_CHAR) {
                buffer = resolveParentesisedPart(Arrays.copyOfRange(function, 0, index));

                int closedQ = 0;
                for (int i = index - 1; i >= 0; i--) {
                    if (function[i] == CLOSE_PARENTHESIS_CHAR) {
                        closedQ++;
                    }
                    if (function[i] == OPEN_PARENTHESIS_CHAR) {
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

    private char[] cutParentesisedPart(char[] function) {
        int countOfCloseDelimiter = 0;

        for (int i = function.length - 1; i >= 0; i--) {
            if (function[i] == CLOSE_PARENTHESIS_CHAR) {
                countOfCloseDelimiter++;
            } else if (function[i] == OPEN_PARENTHESIS_CHAR) {
                countOfCloseDelimiter--;
                if (countOfCloseDelimiter == 0) {
                    return Arrays.copyOfRange(function, 0, i);
                }
            }
        }

        throw new RuntimeException();
    }
}
