package com.markgenerator.markparsers.catalog.mark.parsers.uz;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;

import java.util.Optional;
import java.util.regex.Matcher;

public class UZDrugsMarkParser extends AbstractMarkParser {

    @Override
    public MarkType getType() {
        return MarkType.DRUGS;
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
            .ean(matcher.group(GTIN))
            .verificationKey(matcher.group(VERIFICATION_KEY))
            .verificationCode(matcher.group(VERIFICATION_CODE));

        return Optional.of(builder.build());
    }

}
