package com.jianxu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTests {

    private static Calculator calculator = new CalculatorImpl();

    // step 1
    @Test
    void givenTwoNumberMaxSeparatedByCommas_whenCalculate_thenReturnSum() {
        calculateSum("", 0);
        calculateSum("1", 1);
        calculateSum("1,2", 3);
    }

    // step 2
    @Test
    void givenMultiNumberSeparatedByCommas_whenCalculate_thenReturnSum() {
        calculateSum("1,2,3,4,5,6,7,8,9,10", 55);
    }

    // step 3
    @Test
    void givenNewLinesBetweenNumbers_whenCalculate_thenReturnSum() {
        calculateSum("1\n2,3\n4", 10);
    }

    // step 4
    @Test
    void givenDifferentDelimiters_whenCalculate_thenReturnSum() {
        calculateSum("//;\n1;2\n3", 6);
    }

    // step 5
    @Test
    void givenNegativeNumbers_whenCalculate_thenThrowIllegalArgumentException() {
        Throwable throwable = calculateSumException("//;\n1;2;-3;-4");
        assertEquals("negatives not allowed: -3,-4", throwable.getMessage());
    }

    // step 6
    @Test
    void givenNumberBiggerThen1000_whenCalculate_thenNumberBiggerThen1000IsIgnoredAndReturnSum() {
        calculateSum("//;\n1;2;1002", 3);
    }

    // step 7
    @Test
    void givenDelimiterLengthBiggerThen1InFormat_whenCalculate_thenReturnSum() {
        calculateSum("//[***]\n1***2***3", 6);
    }

    // step 8
    @Test
    void givenMultiDelimiterLengthEqualTo1_whenCalculate_thenReturnSum() {
        calculateSum("//[*][%]\n1*2%3", 6);
    }

    // step 9
    @Test
    void givenMultiDelimiterLengthLongerThen1_whenCalculate_thenReturnSum() {
        calculateSum("//[***][%%]\n1***2%%3%%4***5", 15);
    }

    private void calculateSum(String input, int ResultExpected) {
        assertEquals(ResultExpected, calculator.add(input));
    }

    private Throwable calculateSumException(String input) {
        return Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.add(input));
    }
}
