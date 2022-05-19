package com.markgenerator.markparsers.catalog.mark.parsers.ru;


import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;

import java.util.regex.Pattern;


public class PhotoMarkParser extends AbstractMarkParser {

    private final Pattern pattern = Pattern.compile("^" +
            "01(?<" + GTIN + ">\\d{14})" +
            "21(?<" + SERIAL + ">\\S{20})" +
            "(?:91(?<" + VERIFICATION_KEY + ">\\S{4}))" +
            "(?:92(?<" + VERIFICATION_CODE + ">\\S{44}))" +
            "$");

    @Override
    public MarkType getType() {
        return MarkType.PHOTO;
    }

    @Override
    protected Pattern getDefaultPattern() {
        return pattern;
    }
}
