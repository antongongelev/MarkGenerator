package com.markgenerator.markparsers.catalog.mark.parsers;

import com.markgenerator.markingapi.catalog.mark.MarkData;
import com.markgenerator.markingapi.catalog.mark.MarkParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class AbstractMarkParser implements MarkParser {
    private static final Logger log = LoggerFactory.getLogger(AbstractMarkParser.class);

    protected static final String GTIN_PREFIX = "gtinPrefix";
    protected static final String AI_GTIN_CODE = "01";
    protected static final String AI_SERIAL_NUMBER_CODE = "21";
    protected static final String GTIN = "gtin";
    protected static final String SERIAL = "serial";
    protected static final String VERIFICATION_KEY = "verificationKey";
    protected static final String VERIFICATION_CODE = "verificationCode";

    /**
     * Паттерн для разбора марки по умолчанию.
     * Для большинства типов маркировок он совпадает.
     */
    protected static final Pattern defaultPattern = Pattern.compile("^" +
            "01(?<" + GTIN + ">\\d{14})" +
            "21(?<" + SERIAL + ">\\S{13})" +
            "(?:91(?<" + VERIFICATION_KEY + ">\\S{4}))" +
            "(?:92(?<" + VERIFICATION_CODE + ">\\S{44}))" +
            "$");

    /**
     * Паттерн для разбора марки определенный пользователем.
     */
    protected Pattern userDefinedPattern;

    @Override
    public void applyPattern(String userDefinedRegex) {
        if (StringUtils.isBlank(userDefinedRegex)) {
            userDefinedPattern = null;
            return;
        }

        try {
            userDefinedPattern = Pattern.compile(userDefinedRegex);
        } catch (PatternSyntaxException ex) {
            log.warn("Cannot compile user defined regex: '{}'!", userDefinedRegex);
            userDefinedPattern = null;
        }
    }

    /**
     * Базовая реализация разбора марки.
     * Подходит для большинства типов маркировок.
     */
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
                                                 .verificationKey(matcher.group(VERIFICATION_KEY))
                                                 .verificationCode(matcher.group(VERIFICATION_CODE));

        return Optional.of(builder.build());
    }

    /**
     * Вернет уникальное наименование парсера.
     * Используется в качестве наименования модуля для получения настроек парсеров.
     * <p>
     * По умолчанию возвращает строковое представление markType в upperCase.
     */
    @Override
    public String getParserName() {
        return getType().name().toUpperCase();
    }

    @Override
    public Optional<String> parseMarkWithCutTail(String rawMark) {
        final Matcher matcher = getCurrentPattern().matcher(rawMark);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional.of(AI_GTIN_CODE + matcher.group(GTIN) + AI_SERIAL_NUMBER_CODE + matcher.group(SERIAL));
    }

    // подходит для большинства типов КМ: фото, парфюм, шины, обувь
    @Override
    public String concatMark(MarkData markData, String gs) {
        StringBuilder sb = new StringBuilder();
        sb.append("01").append(markData.getGtin())
          .append("21").append(markData.getSerialNumber());
        if (markData.getVerificationKey() != null && markData.getVerificationCode() != null) {
            sb.append(gs)
              .append("91").append(markData.getVerificationKey())
              .append(gs)
              .append("92").append(markData.getVerificationCode());
        }
        return sb.toString();
    }


    /**
     * Вернет базовый паттерн (по умолчанию) для разбора марок соответствующий этому парсеру.
     */
    protected Pattern getDefaultPattern() {
        return defaultPattern;
    }

    /**
     * Вернет текущий паттерн для разбора марок соответствующий этому парсеру.
     * <p>
     * Если пользователь определил собственный паттерн, то вернет его.
     * В ином случае, вернет паттерн по умолчанию.
     */
    public Pattern getCurrentPattern() {
        return Optional.ofNullable(userDefinedPattern)
                       .orElse(getDefaultPattern());
    }
}

