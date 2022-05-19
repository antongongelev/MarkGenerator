package com.markgenerator.markparsers.catalog.mark.parsers.ru;


import java.util.regex.Pattern;

/**
 * Парсер марок молочной продукции (Беларусь) для касс в РФ
 *
 * @see <a href="https://crystals.atlassian.net/browse/SRTB-5599">SRTB-5599</a>
 */
public class BYMilkMarkParserRU extends MilkMarkParser {

    private static final String PARSER_NAME = "BY_MILK";

    private final Pattern defaultPattern = Pattern.compile("^" +
        "01(?<" + GTIN + ">\\d{14})" +
        "21(?<" + SERIAL + ">(\\S{6}|\\S{8}))" +
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

}
