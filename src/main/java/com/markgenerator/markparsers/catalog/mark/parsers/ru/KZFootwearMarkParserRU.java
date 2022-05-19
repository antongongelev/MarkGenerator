package com.markgenerator.markparsers.catalog.mark.parsers.ru;


import com.markgenerator.markparsers.catalog.mark.parsers.kz.KZFootwearMarkParser;

import java.util.regex.Pattern;

/**
 * Парсер марок обуви (Казахастан) для касс в РФ
 *
 * @see <a href="https://crystals.atlassian.net/browse/SRTB-5599">SRTB-5599</a>
 */

public class KZFootwearMarkParserRU extends KZFootwearMarkParser {

    private static final String PARSER_NAME = "KZ_FOOTWEAR";

    private final Pattern pattern = Pattern.compile("^" +
            "01(?<" + GTIN + ">\\d{14})" +
            "21(?<" + SERIAL + ">\\S{13})" +
            "91(?<" + VERIFICATION_KEY + ">\\S{4})" +
            "92(?<" + VERIFICATION_CODE + ">\\S{88})" +
            "$");

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }

}
