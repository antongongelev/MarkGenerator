package com.markgenerator.markparsers.catalog.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BaseNCoder {

    private static final Logger log = LoggerFactory.getLogger(BaseNCoder.class);

    private final String alphabet;

    BaseNCoder(final String alphabet) {
        this.alphabet = alphabet;
    }

    public String encode(int value) {
        StringBuilder builder = new StringBuilder();
        int result = value;
        while (result > 0) {
            int rmdr = result % alphabet.length();
            result = result / alphabet.length();
            builder.insert(0, alphabet.charAt(rmdr));
        }
        return builder.insert(0, alphabet.charAt(result)).toString();
    }

    public Optional<Long> decode(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            int characterIndex = alphabet.indexOf(value.charAt(i));
            if (characterIndex < 0) {
                log.error("Invalid character: '{}' (not in range '{}')", value.charAt(i), alphabet);
                return Optional.empty();
            }
            result += (int) (Math.pow(alphabet.length(), value.length() - 1 - i) * characterIndex);
        }
        return Optional.of(result);
    }
}
