package com.markgenerator.markparsers.catalog.mark.parsers.ru;


/**
 * Парсер марок "Шины (Беларусь)" для касс в РФ
 *
 * @see <a href="https://crystals.atlassian.net/browse/SRTB-5610">SRTB-5610</a>
 */
public class BYTyresMarkParserRU extends TyresMarkParser {

    private static final String PARSER_NAME = "BY_TYRES";

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }
}
