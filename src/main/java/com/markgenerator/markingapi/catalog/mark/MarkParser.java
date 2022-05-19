package com.markgenerator.markingapi.catalog.mark;

import com.markgenerator.markingapi.catalog.MarkType;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Интерфейс парсеров марок
 */
public interface MarkParser {

    /**
     * Вернуть тип маркированного товара, марки которого разбирает данный парсер
     */
    MarkType getType();

    /**
     * Разобрать переданную исходную (нормаллизованную, без разделителей) марку
     *
     * @return разобранная марка, если получилось разобрать
     */
    Optional<MarkData> parse(String rawMark);

    /**
     * Разбирает марку по регулярным выражениям и возвращает марку с обрезанным криптохвостом
     *
     * @return - разобранную марку с обрезанным криптохвостом, если получилось разобрать
     * - Optional.EMPTY, если не получилось разобрать или если этот тип марки не нужно обрезать
     */
    Optional<String> parseMarkWithCutTail(String rawMark);

    /**
     * Обратно собирает марку с использованием переданного сепаратора
     *
     * @param markData - данные разобранной ранее марки
     * @param gs       - сепаратор (зависит от ФР)
     */
    String concatMark(MarkData markData, String gs);

    /**
     * Имя парсера. Используется для определения соотношения парсеров и определенных пользователем регулярных выражений.
     */
    String getParserName();

    /**
     * Позволяет переопределить регулярное выражение парсера.
     *
     * @param userDefinedRegex - пользовательское регулярное выражение для парсера.
     *                         В случае невозможности применить паттерн - будет использоваться паттерн по-умолчанию
     */
    void applyPattern(String userDefinedRegex);

    /**
     * @return паттерн
     */
    Pattern getCurrentPattern();
}

