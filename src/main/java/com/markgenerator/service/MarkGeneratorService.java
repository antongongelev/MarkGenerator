package com.markgenerator.service;

import com.github.curiousoddman.rgxgen.RgxGen;
import com.markgenerator.dto.MarkRequest;
import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkParser;
import com.markgenerator.markingapi.catalog.mark.MarkParserCountry;
import com.markgenerator.markparsers.catalog.mark.parsers.MarkParsersFactory;
import com.markgenerator.utils.PatternParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MarkGeneratorService {

    @Autowired
    private PatternParser patternParser;

    private Map<MarkParserCountry, Set<MarkParser>> parsers;

    @PostConstruct
    private void init() {
        parsers = Arrays.stream(MarkParserCountry.values())
                        .collect(Collectors.toMap(k -> k, v -> MarkParsersFactory.getParsersByCountry(v)
                                                                                 .stream()
                                                                                 .filter(p -> p.getType() != MarkType.JEWELRY)
                                                                                 .filter(distinctByKey(MarkParser::getType))
                                                                                 .collect(Collectors.toSet())));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public String generateByMarkType(MarkRequest markRequest) {
        try {
            MarkParser parser = parsers.get(markRequest.getCountry())
                                       .stream()
                                       .filter(p -> p.getType() == markRequest.getMarkType())
                                       .findFirst()
                                       .orElseThrow(() -> new RuntimeException("Can't find parser for mark type -> " + markRequest.getMarkType()));
            String pattern = parser.getCurrentPattern().toString();
            String parsedPattern = patternParser.parse(pattern);
            String withoutGtin = new RgxGen(parsedPattern).generate();
            if (StringUtils.isBlank(markRequest.getBarcode())) {
                return withoutGtin;
            } else {
                return patternParser.applyBarcode(pattern, withoutGtin, markRequest.getBarcode());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return "Упс, что-то пошло не по плану";
        }
    }

    public List<String> getCountries() {
        MarkParserCountry[] countries = MarkParserCountry.values();
        return Arrays.stream(countries).map(Enum::toString).collect(Collectors.toList());
    }

    public List<String> getMarkTypesByCountry(MarkParserCountry country) {
        return parsers.get(country).stream().map(p -> p.getType().toString()).collect(Collectors.toList());
    }
}
