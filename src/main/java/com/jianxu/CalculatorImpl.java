package com.jianxu;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class CalculatorImpl implements Calculator{
    private static final Pattern MULTI_DELIMITER_PATTERN = Pattern.compile("\\[(.+?)\\]");

    @Override
    public int add(String numbers) {
        if (Strings.isNullOrEmpty(numbers)) {
            return 0;
        }

        // default delimiter and numbers input to parse
        Set<String> delimiters = Sets.newHashSet(",");
        String numbersParts = numbers;

        if (numbers.startsWith("//")) {
            final int delimitersNewLineIndex = numbers.indexOf("\n");
            final String delimiterParts = numbers.substring(0, delimitersNewLineIndex);
            delimiters = parseDelimiters(delimiterParts);
            numbersParts = numbers.substring(delimitersNewLineIndex + 1);
        }

        final String [] numbersSplitted = numbersParts.split(buildNumberInputRegex(delimiters));

        checkNegativeValues(numbersSplitted);

        return Arrays.stream(numbersSplitted)
                .map(String::trim)
                .map(Integer::parseInt)
                .filter(integer -> integer <= 1000)
                .reduce(0, Integer::sum);
    }

    private void checkNegativeValues(String[] numbersSplitted) {
        String negativeValues = Arrays.stream(numbersSplitted)
                .map(String::trim)
                .map(Integer::parseInt)
                .filter(integer -> integer < 0)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        if(!Strings.isNullOrEmpty(negativeValues)) {
            throw new IllegalArgumentException("negatives not allowed: " + negativeValues);
        }
    }

    private Set<String> parseDelimiters(String input) {
        final String delimitersContent = input.split("//")[1];
        // Multi delimiter
        final Matcher matcher = MULTI_DELIMITER_PATTERN.matcher(delimitersContent);
        final Set<String> delimiterSet = new HashSet<>();
        while (matcher.find()) {
            delimiterSet.add(matcher.group(1));
        }

        // if not multi delimiter pattern
        if (delimiterSet.isEmpty()) {
            delimiterSet.add(delimitersContent);
        }
        return delimiterSet;
    }

    private String buildNumberInputRegex (Set<String> delimiters) {
        return String.format("[%s]+|[\n]", String.join("|", delimiters));
    }

}
