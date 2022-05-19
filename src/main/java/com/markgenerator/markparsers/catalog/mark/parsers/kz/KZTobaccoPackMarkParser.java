package com.markgenerator.markparsers.catalog.mark.parsers.kz;

import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.CiggyPackMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок пачек сигарет (Казахастан) для касс в Казахстане
 * <p>
 * По условиям SRTZ-648 отличается от РФ отсутствием МРЦ
 */
public class KZTobaccoPackMarkParser extends CiggyPackMarkParser {

    private final Pattern pattern = Pattern.compile(
            "^(?<" + AbstractMarkParser.GTIN + ">\\d{14})" +
                    "(?<" + AbstractMarkParser.SERIAL + ">\\S{7})" +
                    "(?<" + AbstractMarkParser.VERIFICATION_CODE + ">\\S{8})$");

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
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
                                                 .serialNumber(matcher.group(AbstractMarkParser.SERIAL))
                                                 .ean(StringUtils.stripStart(matcher.group(AbstractMarkParser.GTIN), "0"));
        return Optional.of(builder.build());
    }

    @Override
    public Optional<String> parseMarkWithCutTail(String rawMark) {
        return Optional.empty();
    }
}
