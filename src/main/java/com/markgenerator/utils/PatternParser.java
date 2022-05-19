package com.markgenerator.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PatternParser {
    public String parse(String pattern) {
        return pattern.replace("?<gtin>", StringUtils.EMPTY)
                      .replace("?<serial>", StringUtils.EMPTY)
                      .replace("?<mrp>", StringUtils.EMPTY)
                      .replace("?<tradeCode>", StringUtils.EMPTY)
                      .replace("?<shelfLife>", StringUtils.EMPTY)
                      .replace("?<checkCode>", StringUtils.EMPTY)
                      .replace("?<gtinPrefix>", StringUtils.EMPTY)
                      .replace("?<verificationKey>", StringUtils.EMPTY)
                      .replace("?<verificationCode>", StringUtils.EMPTY);
    }

    public String applyBarcode(String pattern, String mark, String barcode) {
        List<String> strings = Arrays.asList(pattern.split("\\(\\?"));
        String gtinPart = strings.stream().filter(s -> s.contains("<gtin>")).findFirst().get();
        int startFrom = strings.get(0).substring(1).length();
        int duration = Integer.parseInt(String.valueOf(gtinPart.subSequence(gtinPart.indexOf("{") + 1, gtinPart.indexOf("}"))));
        if (barcode.length() > duration) {
            throw new IllegalArgumentException("ШК товара слишком длинный");
        }
        return mark.substring(0, startFrom) + StringUtils.leftPad(barcode, duration, "0") + mark.substring(startFrom + duration);
    }
}
