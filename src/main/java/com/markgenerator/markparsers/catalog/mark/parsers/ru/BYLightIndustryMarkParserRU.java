package com.markgenerator.markparsers.catalog.mark.parsers.ru;


/**
 * Парсер марок "Легкая промышленность (Беларусь)" для касс в РФ
 *
 * @see <a href="https://crystals.atlassian.net/browse/SRTB-5610">SRTB-5610</a>
 */
public class BYLightIndustryMarkParserRU extends LightIndustryMarkParser {

    private static final String PARSER_NAME = "BY_LIGHT_INDUSTRY";

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }
}
