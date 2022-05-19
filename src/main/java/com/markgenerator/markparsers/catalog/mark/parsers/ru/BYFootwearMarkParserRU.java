package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.mark.MarkData;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок "Обувь (Беларусь)" для касс в РФ
 *
 * @see <a href="https://crystals.atlassian.net/browse/SRTB-5610">SRTB-5610</a>
 */
public class BYFootwearMarkParserRU extends FootwearMarkParser {

    private static final String PARSER_NAME = "BY_FOOTWEAR";

    private final Pattern pattern = Pattern.compile("^" +
        "01(?<" + GTIN + ">\\d{14})" +
        "21(?<" + SERIAL + ">\\S{13})" +
        "(?:91(?<" + VERIFICATION_KEY + ">\\S{5}))" +
        "(?:92(?<" + VERIFICATION_CODE + ">\\S{88}))" +
        "$");

    @Override
    public Optional<MarkData> parse(String rawMark) {
        final Matcher matcher = getCurrentPattern().matcher(rawMark);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        final MarkData.Builder builder = MarkData.newBuilder()
            .parser(this)
            .rawMark(rawMark)
            .markType(getType())
            .serialNumber(matcher.group(SERIAL))
            .ean(StringUtils.stripStart(matcher.group(GTIN), "0"))
            .verificationKey(matcher.group(VERIFICATION_KEY))
            .verificationCode(matcher.group(VERIFICATION_CODE));

        return Optional.of(builder.build());
    }

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }
}
