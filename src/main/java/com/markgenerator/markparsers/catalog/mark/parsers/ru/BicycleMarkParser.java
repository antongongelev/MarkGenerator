package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;

public class BicycleMarkParser extends AbstractMarkParser {

    @Override
    public MarkType getType() {
        return MarkType.BICYCLES;
    }

}
