package ua.den.service;

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
        function = new String(delimitSpecSymbols());

        return functionSolver.solve(this).setScale(scale, roundingMethod);
    }

    private char[] delimitSpecSymbols() {
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
                    || OPEN_DELIMITER.equals(bufferValue)
                    || CLOSE_DELIMITER.equals(bufferValue)
                    || bufferValue.matches("(\\d|\\.)+")) {
                if (operandsToDelimit.contains(buffer.toString())) {
                    String operand = buffer.toString();

                    if (operand.equals("*") || operand.equals("/")) {
                        processMultiplicationAndDivisionDelimit(result, counter);
                        counter++;
                    } else if (operand.equals("^")) {
                        processPowerDelimit(result, counter);
                    }
                }

                buffer.setLength(0);
            }

            counter++;
        }

        return result.toString().toCharArray();
    }

    private void processMultiplicationAndDivisionDelimit(StringBuilder function, int position) {
        boolean openedDelimiter = false;
        boolean closestDelimiterFound = false;
        int closedDelimiterCounter = 0;
        int closestDelimiterPosition = 0;

        for (int i = position - 1; i >= 0; i--) {
            if (function.charAt(i) == CLOSE_DELIMITER_CHAR) {
                closedDelimiterCounter++;
            } else if (function.charAt(i) == OPEN_DELIMITER_CHAR) {
                if (closedDelimiterCounter > 0) {
                    closedDelimiterCounter--;
                } else {
                    closestDelimiterFound = true;
                    closestDelimiterPosition = i;
                    break;
                }
            } else if (function.charAt(i) == '+' || function.charAt(i) == '-') {
                if (closedDelimiterCounter == 0) {
                    function.insert(i + 1, "(");
                    position++;
                    openedDelimiter = true;
                    break;
                }
            }
        }

        int openedDelimiterCount = 0;
        boolean closedDelimited = false;

        for (int i = position + 1; i < function.length(); i++) {
            if (function.charAt(i) == '+' || function.charAt(i) == '-') {
                if (openedDelimiterCount == 0) {
                    function.insert(i, ")");

                    if (!openedDelimiter) {
                        if (closestDelimiterFound) {
                            function.insert(closestDelimiterPosition, "(");
                            openedDelimiter = true;
                        } else {
                            function.insert(0, "(");
                            openedDelimiter = true;
                        }
                    }

                    closedDelimited = true;

                    break;
                }
            } else if (function.charAt(i) == OPEN_DELIMITER_CHAR) {
                openedDelimiterCount++;
            } else if (function.charAt(i) == CLOSE_DELIMITER_CHAR) {
                if (openedDelimiterCount > 0) {
                    openedDelimiterCount--;
                } else if (openedDelimiter) {
                    function.insert(i, ")");
                    closedDelimited = true;
                    break;
                } else {
                    break;
                }
            }
        }

        if (openedDelimiter && !closedDelimited) {
            function.insert(function.length(), ")");
        }
    }

    private void processPowerDelimit(StringBuilder function, int position) {
        boolean openedDelimiter = false;
        boolean closestDelimiterFound = false;
        int closedDelimiterCounter = 0;
        int closestDelimiterPosition = 0;

        for (int i = position - 1; i >= 0; i--) {
            if (function.charAt(i) == CLOSE_DELIMITER_CHAR) {
                closedDelimiterCounter++;
            } else if (function.charAt(i) == OPEN_DELIMITER_CHAR) {
                if (closedDelimiterCounter > 0) {
                    closedDelimiterCounter--;
                } else {
                    closestDelimiterFound = true;
                    closestDelimiterPosition = i;
                    break;
                }
            } else if (function.charAt(i) == '+' || function.charAt(i) == '-' || function.charAt(i) == '*' || function.charAt(i) == '/') {
                if (closedDelimiterCounter == 0) {
                    function.insert(i + 1, "(");
                    position++;
                    openedDelimiter = true;
                    break;
                }
            }
        }

        int openedDelimiterCount = 0;
        boolean closedDelimited = false;

        for (int i = position + 1; i < function.length(); i++) {
            if (function.charAt(i) == '+' || function.charAt(i) == '-' || function.charAt(i) == '*' || function.charAt(i) == '/') {
                if (openedDelimiterCount == 0) {
                    function.insert(i, ")");

                    if (!openedDelimiter) {
                        if (closestDelimiterFound) {
                            function.insert(closestDelimiterPosition, "(");
                            openedDelimiter = true;
                        } else {
                            function.insert(0, "(");
                            openedDelimiter = true;
                        }
                    }

                    closedDelimited = true;

                    break;
                }
            } else if (function.charAt(i) == OPEN_DELIMITER_CHAR) {
                openedDelimiterCount++;
            } else if (function.charAt(i) == CLOSE_DELIMITER_CHAR) {
                if (openedDelimiterCount > 0) {
                    openedDelimiterCount--;
                } else if (openedDelimiter) {
                    function.insert(i, ")");
                    closedDelimited = true;
                    break;
                } else {
                    break;
                }
            }
        }

        if (openedDelimiter && !closedDelimited) {
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
