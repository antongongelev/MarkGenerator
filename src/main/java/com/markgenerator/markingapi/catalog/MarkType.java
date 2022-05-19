package com.markgenerator.markingapi.catalog;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Тип маркированного товара
 */
public enum MarkType {
    /**
     * Неизвестный тип маркированного товара
     */
    UNKNOWN(null, null, null),

    /**
     * Табачная продукция
     * <p>
     * 30 - "о реализуемом подакцизном товаре, подлежащем маркировке средством идентификации, не имеющем кода маркировки"
     * 31 - "о реализуемом подакцизном товаре, подлежащем маркировке средством идентификации, имеющем код маркировки"
     */
    TOBACCO("444D", 31, 30),

    /**
     * Обувь
     */
    FOOTWEAR("444D"),

    /**
     * Парфюмерия
     */
    PERFUMES("444D"),

    /**
     * Легкая промышленность
     */
    LIGHT_INDUSTRY("444D"),

    /**
     * Шины и диски
     */
    TYRES("444D"),

    /**
     * Фототехника и вспышки
     */
    PHOTO("444D"),

    /**
     * Молочная продукция
     */
    MILK("444D"),

    /**
     * Драгоценные металлы и драгоценные камни
     * P.S.: пока в ОФД не передаем, код не известен
     * <p>
     * По условиям SRTZ-1115 для ювелирных изделий передаем признак предмета расчета из карточки товара
     */
    JEWELRY("", null, null),

    /**
     * Бутилированная вода
     */
    WATER("444D"),

    /**
     * Велосипеды
     */
    BICYCLES("444D"),

    /**
     * Вода и напитки
     */
    WATER_AND_BEVERAGES(null),

    /**
     * Лекарства
     */
    DRUGS(null);

    /**
     * "о реализуемом товаре, подлежащем маркировке средством идентификации, не имеющем кода маркировки,
     * за исключением подакцизного товара"
     */
    private static final int DEFAULT_CALC_SUBJECT_NO_MARK = 32;

    /**
     * "о реализуемом товаре, подлежащем маркировке средством идентификации, имеющем код маркировки,
     * за исключением подакцизного товара"
     */
    private static final int DEFAULT_CALC_SUBJECT_WITH_MARK = 33;

    /**
     * Код маркировки (используется для отправки в ОФД и печати на чеке)
     */
    private final String markCode;

    /**
     * Признак предмета расчета при продаже товара, подлежашего маркировке, но без марки (для ФФД 1.2),
     * если задан, то является приоритетным перед признаком из карточки товара или внешней системы
     */
    private final Integer calculationSubjectNoMark;

    /**
     * Признак предмета расчета при продаже маркированного товара с маркой (для ФФД 1.2),
     * если задан, то является приоритетным перед признаком из карточки товара или внешней системы
     */
    private final Integer calculationSubjectWithMark;

    private static final Map<String, MarkType> mappedByValue = Arrays.stream(MarkType.values())
                                                                     .collect(Collectors.toMap(MarkType::name, Function.identity()));

    private static final Map<String, MarkType> markTypesByProductTypes = Collections.singletonMap("ProductCiggyEntity", TOBACCO);

    MarkType(String markCode) {
        this(markCode, DEFAULT_CALC_SUBJECT_WITH_MARK, DEFAULT_CALC_SUBJECT_NO_MARK);
    }

    MarkType(String markCode, Integer calculationSubjectWithMark, Integer calculationSubjectNoMark) {
        this.markCode = markCode;
        this.calculationSubjectWithMark = calculationSubjectWithMark;
        this.calculationSubjectNoMark = calculationSubjectNoMark;
    }

    public static MarkType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return mappedByValue.getOrDefault(name.toUpperCase(), UNKNOWN);
    }

    /**
     * Вернет в порядке приоритета
     * 1. Для null или пустого значения вернет тип маркировки, соответствующий productType (если есть) или null
     * 2. Для корректного name (не null, не пустое значние, не UNKNOWN) всегда вернет соответствующий тип маркировки
     * 3. Для UNKNOWN вернет тип маркировки, соответствующий productType (если есть) или UNKNOWN
     */
    public static MarkType getByName(String name, String productType) {
        if (StringUtils.isBlank(name)) {
            return getByProductType(productType);
        }
        final MarkType byName = mappedByValue.getOrDefault(name.toUpperCase(), UNKNOWN);
        if (byName == UNKNOWN) {
            final MarkType byProductType = getByProductType(productType);
            if (byProductType != null) {
                return byProductType;
            }
        }
        return byName;
    }

    public static MarkType getByOrdinal(Integer ordinal, String productType) {
        if (ordinal == null) {
            return getByProductType(productType);
        }
        final MarkType byOrdinal = getByOrdinal(ordinal);
        if (byOrdinal == UNKNOWN) {
            final MarkType byProductType = getByProductType(productType);
            if (byProductType != null) {
                return byProductType;
            }
        }
        return byOrdinal;
    }

    private static MarkType getByProductType(String productType) {
        if (StringUtils.isBlank(productType)) {
            return null;
        }
        return markTypesByProductTypes.get(productType.trim());
    }

    public static MarkType getByOrdinal(Integer ordinal) {
        if (ordinal == null) {
            return null;
        }
        final MarkType[] values = values();
        if (ordinal >= values.length || ordinal < 0) {
            return UNKNOWN;
        }
        return values[ordinal];
    }

    /**
     * Проверяет, является ли признак маркировки дефолтным для заданного типа товара (чтобы не расхлопывать товары, которые фактически одинаковы, но формально имеют
     * разные значения mark_type в БД
     */
    public static boolean isDefaultMarkType(MarkType markType, String productType) {
        if (markType == null || markType == UNKNOWN) {
            return true;
        }
        final MarkType byProductType = getByProductType(productType);
        return byProductType != null && markType == byProductType;
    }

    public String getMarkCode() {
        return markCode;
    }

    public Integer getCalculationSubjectNoMark() {
        return calculationSubjectNoMark;
    }

    public Integer getCalculationSubjectWithMark() {
        return calculationSubjectWithMark;
    }
}
