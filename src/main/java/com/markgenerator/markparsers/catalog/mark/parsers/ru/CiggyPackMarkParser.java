package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.codec.CodecUtils;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер марок пачек сигарет
 */
public class CiggyPackMarkParser extends AbstractMarkParser {

    private static final String PARSER_NAME = "TOBACCO_PACK";

    protected static final String MRP = "mrp";

    private static final int MRP_MAX_VALUE = 500000;

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
        mrp.ifPresent(m -> {
            decode(m).ifPresent(builder::minimalRetailPrice);
            builder.rawMinimalRetailPrice(m);
        });
        Optional<String> verificationCode = Optional.ofNullable(matcher.group(VERIFICATION_CODE));
        verificationCode.ifPresent(builder::verificationCode);

        return Optional.of(builder.build());
    }

    private Optional<Long> decode(String value) {
        return CodecUtils.getBase80Coder().decode(value)
                         .filter(this::isValidMrp);
    }

    private boolean isValidMrp(long decodedMrp) {
        //МРЦ табака не может содержать копеек, быть больше 5000 и меньше или равным 0 (защита от старых марок, где те же 4 знака имели другой смысл)
        return decodedMrp <= MRP_MAX_VALUE && decodedMrp > 0 && decodedMrp % 100 == 0;
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

        return Optional.of(matcher.group(GTIN) + matcher.group(SERIAL));
    }

    @Override
    public String concatMark(MarkData markData, String gs) {
        // для пачек не используем GS, это не GS-1 DataMatrix
        StringBuilder sb = new StringBuilder();
        sb.append(markData.getGtin())
          .append(markData.getSerialNumber());
        if (markData.getRawMinimalRetailPrice() != null) {
            sb.append(markData.getRawMinimalRetailPrice());
        }
        if (markData.getVerificationCode() != null) {
            sb.append(markData.getVerificationCode());
        }
        return sb.toString();
    }
}
