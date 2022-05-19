package com.markgenerator.markparsers.catalog.mark.parsers.ru;


import com.markgenerator.markingapi.catalog.mark.MarkData;

import java.util.regex.Pattern;

/**
 * Парсер марок скоропортящейся молочной продукции
 */
public class MilkPerishableMarkParser extends MilkWithShelfLifeMarkParser {

    private static final String PARSER_NAME = "MILK_PERISHABLE";

    private final Pattern defaultPattern = Pattern.compile("^" +
        "01(?<" + GTIN + ">\\d{14})" +
        "21(?<" + SERIAL + ">\\S{13})" +
        "7003(?<" + SHELF_LIFE + ">\\S{8})" +
        "93(?<" + VERIFICATION_KEY + ">\\S{4})" +
        "$");

    @Override
    protected Pattern getDefaultPattern() {
        return defaultPattern;
    }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }

    @Override
    public String concatMark(MarkData markData, String gs) {
        StringBuilder sb = new StringBuilder();
        sb.append("01").append(markData.getGtin())
            .append("21").append(markData.getSerialNumber());
        if (markData.getShelfLife() != null) {
            sb.append(gs)
                .append("7003").append(markData.getShelfLife());
        }
        if (markData.getVerificationKey() != null) {
            sb.append(gs)
                .append("93").append(markData.getVerificationKey());
        }
        return sb.toString();
    }
}
