package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Стандартный парсер марок молочной продукции (без срока годности)
 * Сейчас по факту используется только этот парсер для молока
 */
public class MilkMarkParser extends AbstractMarkParser {

    private final Pattern defaultPattern = Pattern.compile("^" +
            "01(?<" + GTIN + ">\\d{14})" +
            "21(?<" + SERIAL + ">\\S{6})" +
            "93(?<" + VERIFICATION_KEY + ">\\S{4})" +
            "$");

    @Override
    public MarkType getType() {
        return MarkType.MILK;
    }

    @Override
    protected Pattern getDefaultPattern() {
        return defaultPattern;
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
                                                 .ean(StringUtils.stripStart(matcher.group(GTIN), "0"));

        Optional<String> verificationKey = Optional.ofNullable(matcher.group(VERIFICATION_KEY));
        verificationKey.ifPresent(builder::verificationKey);

        return Optional.of(builder.build());
    }

    @Override
    public String concatMark(MarkData markData, String gs) {
        StringBuilder sb = new StringBuilder();
        sb.append("01").append(markData.getGtin())
          .append("21").append(markData.getSerialNumber());
        if (markData.getVerificationKey() != null) {
            sb.append(gs)
              .append("93").append(markData.getVerificationKey());
        }
        return sb.toString();
    }
}
