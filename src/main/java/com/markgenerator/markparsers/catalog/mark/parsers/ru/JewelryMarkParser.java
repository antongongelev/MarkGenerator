package com.markgenerator.markparsers.catalog.mark.parsers.ru;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markparsers.catalog.mark.parsers.AbstractMarkParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JewelryMarkParser extends AbstractMarkParser {

    /*
        Дефолтный паттерн ювелирки - 16 цифр. Но из 16 цифр могут состоять другие штрихкоды, например,
        дисконтные карты или шк-трансформеры, поэтому чтобы парсер не распознавал всё подряд как марку ювелирки,
        по дефолту его задавать не будем.
        Те, кто хотят использовать ювелирку, должны вручную задать регулярку в БД.
     */
    private final Pattern defaultPattern = Pattern.compile("no default pattern");

    @Override
    public MarkType getType() {
        return MarkType.JEWELRY;
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
                                                 .serialNumber(matcher.group())
                                                 .ean(StringUtils.EMPTY);

        return Optional.of(builder.build());
    }

    @Override
    public Optional<String> parseMarkWithCutTail(String rawMark) {
        return Optional.empty();
    }
}
