package com.markgenerator.markparsers.catalog.mark.parsers.kz;

import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.FootwearMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок обуви (Казахастан) для касс в Казахстане
 */
public class KZFootwearMarkParser extends FootwearMarkParser {

    private final Pattern pattern = Pattern.compile("^" +
            "01(?<" + AbstractMarkParser.GTIN + ">\\d{14})" +
            "21(?<" + AbstractMarkParser.SERIAL + ">\\S{13})" +
            "91(?<" + AbstractMarkParser.VERIFICATION_KEY + ">\\S{4})" +
            "92(?<" + AbstractMarkParser.VERIFICATION_CODE + ">\\S{88})" +
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
                                                 .serialNumber(matcher.group(AbstractMarkParser.SERIAL))
                                                 .ean(StringUtils.stripStart(matcher.group(AbstractMarkParser.GTIN), "0"))
                                                 .verificationKey(matcher.group(AbstractMarkParser.VERIFICATION_KEY))
                                                 .verificationCode(matcher.group(AbstractMarkParser.VERIFICATION_CODE));

        return Optional.of(builder.build());
    }

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }

    @Override
    public String concatMark(MarkData markData, String gs) {
        StringBuilder sb = new StringBuilder();
        sb.append("01").append(markData.getGtin());
        sb.append("21").append(markData.getSerialNumber()).append(gs);
        sb.append("91").append(markData.getVerificationKey()).append(gs);
        sb.append("92").append(markData.getVerificationCode());
        return sb.toString();
    }
}
