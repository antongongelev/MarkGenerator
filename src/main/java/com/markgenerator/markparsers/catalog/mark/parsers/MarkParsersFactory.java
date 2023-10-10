package com.markgenerator.markparsers.catalog.mark.parsers;

import com.markgenerator.markingapi.catalog.mark.MarkParser;
import com.markgenerator.markingapi.catalog.mark.MarkParserCountry;
import com.markgenerator.markparsers.catalog.mark.parsers.kz.KZFootwearMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.kz.KZTobaccoPackMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.BYFootwearMarkParserRU;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.BYLightIndustryMarkParserRU;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.BYMilkMarkParserRU;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.BYTyresMarkParserRU;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.BicycleMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.CiggyBlockMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.CiggyPackMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.FootwearMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.JewelryMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.KZFootwearMarkParserRU;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.LightIndustryMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.MilkMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.PerfumesMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.PhotoMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.TyresMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.ru.WaterMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.uz.UZDrugsMarkParser;
import com.markgenerator.markparsers.catalog.mark.parsers.uz.UZWaterAndBeveragesMarkParser;

import java.util.HashSet;
import java.util.Set;

/**
 * Эта штука нужна чтобы сразу собирать коллекции парсеров по странам.
 * Клиентский код должен решить, в какой стране он живет и сможет получить себе все нужные парсеры, чтобы затем уже подписать на настройки/положить в контекст и т.д.
 */
public class MarkParsersFactory {

    public static Set<MarkParser> getParsersByCountry(MarkParserCountry country) {
        switch (country) {
            case KZ:
                return buildKZParsers();
            case RU:
                return buildRUParsers();
            case UZ:
                return buildUZParsers();
            default:
                throw new IllegalArgumentException("MarkParsersCountry not supported: " + country.name());
        }
    }

    private static Set<MarkParser> buildKZParsers() {
        Set<MarkParser> parsers = new HashSet<>();
        parsers.add(new KZFootwearMarkParser());
        parsers.add(new KZTobaccoPackMarkParser());
        return parsers;
    }

    private static Set<MarkParser> buildRUParsers() {
        Set<MarkParser> parsers = new HashSet<>();
        // KZ парсеры в РФ
        parsers.add(new KZFootwearMarkParserRU());

        // РБ парсеры в РФ
        parsers.add(new BYFootwearMarkParserRU());
        parsers.add(new BYLightIndustryMarkParserRU());
        parsers.add(new BYMilkMarkParserRU());
        parsers.add(new BYTyresMarkParserRU());

        parsers.add(new CiggyPackMarkParser());
        parsers.add(new CiggyBlockMarkParser());
        parsers.add(new FootwearMarkParser());
        parsers.add(new JewelryMarkParser());
        parsers.add(new LightIndustryMarkParser());
        parsers.add(new MilkMarkParser());
        parsers.add(new PerfumesMarkParser());
        parsers.add(new PhotoMarkParser());
        parsers.add(new TyresMarkParser());
        parsers.add(new BicycleMarkParser());
        parsers.add(new WaterMarkParser());

        return parsers;
    }

    private static Set<MarkParser> buildUZParsers() {
        Set<MarkParser> parsers = new HashSet<>();

        parsers.add(new UZDrugsMarkParser());
        parsers.add(new UZWaterAndBeveragesMarkParser());

        return parsers;
    }
}
