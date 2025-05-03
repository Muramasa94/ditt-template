package com.example.ditt_template;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.pf4j.Extension;
import org.springframework.stereotype.Service;
import com.example.ditt_sdk.Service.DevToolExtensionPoint;
import com.example.ditt_sdk.Service.DevToolInterface;

@Extension
@Service
public class IntegerBaseConverter implements DevToolInterface<BigIntegerRequest, Map<String, String>>, DevToolExtensionPoint {

    private static final String BASE64_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";
    private static final int[] COMMON_BASES = {2, 8, 10, 16, 64};

    @Override
    public String getName() {
        return "Integer Base Converter";
    }

    @Override
    public String getCategory() {
        return "Converter";
    }

    @Override
    public Map<String, String> execute(BigIntegerRequest input) {
        Map<String, String> results = new HashMap<>();
        BigInteger number = input.getNumber();
        int customBase = input.getCustomBase();

        // Convert to common bases
        for (int base : COMMON_BASES) {
            results.put(String.valueOf(base), convert(number, base));
        }

        // Convert to custom base if valid
        if (customBase >= 2 && customBase <= 64) {
            results.put(String.valueOf(customBase), convert(number, customBase));
        } else {
            results.put("customBaseError", "Invalid custom base. Must be between 2 and 64.");
        }

        return results;
    }

    private String convert(BigInteger number, int base) {
        if (base < 2 || base > 64) {
            throw new IllegalArgumentException("Base must be between 2 and 64.");
        }
        return base == 64 ? toBase64(number) : toCustomBase(number, base);
    }

    private String toBase64(BigInteger number) {
        if (number.equals(BigInteger.ZERO)) return "0";
        
        StringBuilder result = new StringBuilder();
        BigInteger base = BigInteger.valueOf(64);

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = number.divideAndRemainder(base);
            result.append(BASE64_ALPHABET.charAt(divRem[1].intValue()));
            number = divRem[0];
        }

        return result.reverse().toString();
    }

    private String toCustomBase(BigInteger number, int base) {
        final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";

        if (number.equals(BigInteger.ZERO)) return "0";
        if (base > ALPHABET.length()) throw new IllegalArgumentException("Base too large!");

        StringBuilder result = new StringBuilder();
        BigInteger bigBase = BigInteger.valueOf(base);

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = number.divideAndRemainder(bigBase);
            result.append(ALPHABET.charAt(divRem[1].intValue()));
            number = divRem[0];
        }

        return result.reverse().toString();
    }
}