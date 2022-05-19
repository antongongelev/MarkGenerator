package com.markgenerator.markparsers.catalog.mark.parsers.ru;


import java.util.regex.Pattern;

/**
 * Парсер марок молочной продукции без срока годности
 */
public class MilkWithoutShelfLifeMarkParser extends MilkMarkParser {

    private static final String PARSER_NAME = "MILK_WITHOUT_SHELF_LIFE";

    private final Pattern defaultPattern = Pattern.compile("^" +
        "01(?<" + GTIN + ">\\d{14})" +
        "21(?<" + SERIAL + ">\\S{13})" +
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
