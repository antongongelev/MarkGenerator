package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок блока сигарет
 */
public class CiggyBlockMarkParser extends AbstractMarkParser {

    private static final String PARSER_NAME = "TOBACCO_BLOCK";

    private static final String MRP = "mrp";
    private static final String CHECK_CODE = "checkCode";

//    private final Pattern pattern = Pattern.compile("^(?=.{0,150}$)" +
//            "^(?<" + GTIN_PREFIX + ">01|02)(?<" + GTIN + ">\\d{14})" +
//            "21(?<" + SERIAL + ">.{7})" +
//            "(?:8005(?<" + MRP + ">\\d{6}))?" +
//            "((?:93(?<" + VERIFICATION_CODE + ">\\S{4}))" +
//            "|((?:9099(?<" + CHECK_CODE + ">\\S{8})))).*$");

    private final Pattern pattern = Pattern.compile(
            "^(?<" + GTIN + ">\\d{14})" +
                    "(?<" + SERIAL + ">\\S{7})" +
                    "(?<" + MRP + ">\\S{4})" +
                    "(?<" + VERIFICATION_CODE + ">\\S{4})$");

    @Override
    public MarkType getType() {
        return MarkType.TOBACCO;
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

        Optional<String> mrp = Optional.ofNullable(matcher.group(MRP));
        processMrp(mrp).ifPresent(builder::minimalRetailPrice);
        mrp.ifPresent(builder::rawMinimalRetailPrice);

        Optional<String> verificationCode = Optional.ofNullable(matcher.group(VERIFICATION_CODE));
        verificationCode.ifPresent(builder::verificationCode);

        return Optional.of(builder.build());
    }

    /**
     * Конвертация и фильтрация нулевых МРЦ. Максимума для блоков не выставляем
     */
    private Optional<Long> processMrp(Optional<String> rawMrp) {
        return rawMrp.map(Long::valueOf).filter(m -> m > 0L);
    }

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }

    @Override
    public Optional<String> parseMarkWithCutTail(String rawMark) {
        final Matcher matcher = getCurrentPattern().matcher(rawMark);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional.of(matcher.group(GTIN_PREFIX) + matcher.group(GTIN) + AI_SERIAL_NUMBER_CODE + matcher.group(SERIAL));
    }

    @Override
    public String concatMark(MarkData markData, String gs) {
        StringBuilder sb = new StringBuilder();
        sb.append("01").append(markData.getGtin())
          .append("21").append(markData.getSerialNumber());
        if (markData.getRawMinimalRetailPrice() != null || markData.getVerificationCode() != null) {
            sb.append(gs);
        }
        if (markData.getRawMinimalRetailPrice() != null) {
            sb.append("8005").append(markData.getRawMinimalRetailPrice());
        }
        if (markData.getVerificationCode() != null) {
            sb.append("93").append(markData.getVerificationCode());
        }
        return sb.toString();
    }
}
