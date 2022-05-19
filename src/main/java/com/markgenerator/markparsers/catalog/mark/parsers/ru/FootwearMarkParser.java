package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок обуви
 */
public class FootwearMarkParser extends AbstractMarkParser {

    private static final String TRADE_CODE = "tradeCode";

    private final Pattern pattern = Pattern.compile("^" +
            "01(?<" + GTIN + ">\\d{14})" +
            "21(?<" + SERIAL + ">\\S{13})" +
            "(?:240(?<" + TRADE_CODE + ">\\d{4}))?" +
            "(?:91(?<" + VERIFICATION_KEY + ">\\S{4}))" +
            "(?:92(?<" + VERIFICATION_CODE + ">\\S{88}))" +
            "$");

    @Override
    public MarkType getType() {
        return MarkType.FOOTWEAR;
    }

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
                                                 .foreignTradeCode(matcher.group(TRADE_CODE))
                                                 .verificationKey(matcher.group(VERIFICATION_KEY))
                                                 .verificationCode(matcher.group(VERIFICATION_CODE));

        return Optional.of(builder.build());
    }

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }
}
