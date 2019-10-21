package ua.den.service;

import ua.den.exceptions.ParenthesisUnpairedException;

import java.math.BigDecimal;
import java.util.Map;

import static ua.den.service.symbols.FunctionSymbols.*;

public class FunctionAnalyzer {
    private String function;
    private int scale;
    private int roundingMethod;

    private Map<String, BigDecimal> variables;
    private FunctionSolver functionSolver = new FunctionSolver();

    public FunctionAnalyzer(Map<String, BigDecimal> variables) {
        this.variables = variables;
    }

    public BigDecimal solve() {
        checkParenthesis(function);
        function = new String(parenthesiseSpecSymbols());

        return functionSolver.solve(this).setScale(scale, roundingMethod);
    }

    private char[] parenthesiseSpecSymbols() {
        StringBuilder result = new StringBuilder(function);
        removeWhitespaces(result);
        StringBuilder buffer = new StringBuilder();
        int counter = 0;

        while (counter < result.length()) {
            buffer.append(result.charAt(counter));
            String bufferValue = buffer.toString();

            if (mathCharacters.containsKey(bufferValue)
                    || operationCharacters.containsKey(bufferValue)
                    || variables.containsKey(bufferValue)
                    || OPEN_PARENTHESIS.equals(bufferValue)
                    || CLOSE_PARENTHESIS.equals(bufferValue)
                    || bufferValue.matches("(\\d|\\.)+")) {
                if (operandsToDelimit.contains(buffer.toString())) {
                    String operand = buffer.toString();

                    if (operand.equals("*") || operand.equals("/")) {
                        processMultiplicationAndDivisionParenthesis(result, counter);
                        counter++;
                    } else if (operand.equals("^")) {
                        processPowerParenthesis(result, counter);
                    }
                }

                buffer.setLength(0);
            }

            counter++;
        }

        return result.toString().toCharArray();
    }

    private void checkParenthesis(String function) {
        int unpairedDelimitersCount = 0;

        for (char symbol : function.toCharArray()) {
            if (symbol == OPEN_PARENTHESIS_CHAR) {
                unpairedDelimitersCount++;
            } else if (symbol == CLOSE_PARENTHESIS_CHAR) {
                unpairedDelimitersCount--;
            }
        }

        if (unpairedDelimitersCount != 0) {
            throw new ParenthesisUnpairedException();
        }
    }

    private void processMultiplicationAndDivisionParenthesis(StringBuilder function, int position) {
        boolean openedParenthesis = false;
        boolean closestParenthesisFound = false;
        int closedParenthesisCounter = 0;
        int closestParenthesisPosition = 0;

        for (int i = position - 1; i >= 0; i--) {
            if (function.charAt(i) == CLOSE_PARENTHESIS_CHAR) {
                closedParenthesisCounter++;
            } else if (function.charAt(i) == OPEN_PARENTHESIS_CHAR) {
                if (closedParenthesisCounter > 0) {
                    closedParenthesisCounter--;
                } else {
                    closestParenthesisFound = true;
                    closestParenthesisPosition = i;
                    break;
                }
            } else if (function.charAt(i) == '+' || function.charAt(i) == '-') {
                if (closedParenthesisCounter == 0) {
                    function.insert(i + 1, OPEN_PARENTHESIS);
                    position++;
                    openedParenthesis = true;
                    break;
                }
            }
        }

        int openedParenthesisCount = 0;
        boolean closedDelimited = false;

        for (int i = position + 1; i < function.length(); i++) {
            if (function.charAt(i) == '+' || function.charAt(i) == '-') {
                if (openedParenthesisCount == 0) {
                    function.insert(i, CLOSE_PARENTHESIS);

                    if (!openedParenthesis) {
                        if (closestParenthesisFound) {
                            function.insert(closestParenthesisPosition, OPEN_PARENTHESIS);
                            openedParenthesis = true;
                        } else {
                            function.insert(0, OPEN_PARENTHESIS);
                            openedParenthesis = true;
                        }
                    }

                    closedDelimited = true;

                    break;
                }
            } else if (function.charAt(i) == OPEN_PARENTHESIS_CHAR) {
                openedParenthesisCount++;
            } else if (function.charAt(i) == CLOSE_PARENTHESIS_CHAR) {
                if (openedParenthesisCount > 0) {
                    openedParenthesisCount--;
                } else if (openedParenthesis) {
                    function.insert(i, CLOSE_PARENTHESIS);
                    closedDelimited = true;
                    break;
                } else {
                    break;
                }
            }
        }

        if (openedParenthesis && !closedDelimited) {
            function.insert(function.length(), CLOSE_PARENTHESIS);
        }
    }

    private void processPowerParenthesis(StringBuilder function, int position) {
        boolean openedParenthesis = false;
        boolean closestParenthesisFound = false;
        int closedParenthesisCounter = 0;
        int closestParenthesisPosition = 0;

        for (int i = position - 1; i >= 0; i--) {
            if (function.charAt(i) == CLOSE_PARENTHESIS_CHAR) {
                closedParenthesisCounter++;
            } else if (function.charAt(i) == OPEN_PARENTHESIS_CHAR) {
                if (closedParenthesisCounter > 0) {
                    closedParenthesisCounter--;
                } else {
                    closestParenthesisFound = true;
                    closestParenthesisPosition = i;
                    break;
                }
            } else if (function.charAt(i) == '+' || function.charAt(i) == '-' || function.charAt(i) == '*' || function.charAt(i) == '/') {
                if (closedParenthesisCounter == 0) {
                    function.insert(i + 1, OPEN_PARENTHESIS);
                    position++;
                    openedParenthesis = true;
                    break;
                }
            }
        }

        int openedParenthesisCount = 0;
        boolean closedParenthesised = false;

        for (int i = position + 1; i < function.length(); i++) {
            if (function.charAt(i) == '+' || function.charAt(i) == '-' || function.charAt(i) == '*' || function.charAt(i) == '/') {
                if (openedParenthesisCount == 0) {
                    function.insert(i, CLOSE_PARENTHESIS);

                    if (!openedParenthesis) {
                        if (closestParenthesisFound) {
                            function.insert(closestParenthesisPosition, OPEN_PARENTHESIS);
                            openedParenthesis = true;
                        } else {
                            function.insert(0, OPEN_PARENTHESIS);
                            openedParenthesis = true;
                        }
                    }

                    closedParenthesised = true;

                    break;
                }
            } else if (function.charAt(i) == OPEN_PARENTHESIS_CHAR) {
                openedParenthesisCount++;
            } else if (function.charAt(i) == CLOSE_PARENTHESIS_CHAR) {
                if (openedParenthesisCount > 0) {
                    openedParenthesisCount--;
                } else if (openedParenthesis) {
                    function.insert(i, CLOSE_PARENTHESIS);
                    closedParenthesised = true;
                    break;
                } else {
                    break;
                }
            }
        }

        if (openedParenthesis && !closedParenthesised) {
            function.insert(function.length(), ")");
        }
    }

    private void removeWhitespaces(StringBuilder stringBuilder) {
        int j = 0;
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (!Character.isWhitespace(stringBuilder.charAt(i))) {
                stringBuilder.setCharAt(j++, stringBuilder.charAt(i));
            }
        }
        stringBuilder.delete(j, stringBuilder.length());
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getRoundingMethod() {
        return roundingMethod;
    }

    public void setRoundingMethod(int roundingMethod) {
        this.roundingMethod = roundingMethod;
    }

    public Map<String, BigDecimal> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, BigDecimal> variables) {
        this.variables = variables;
    }
}
