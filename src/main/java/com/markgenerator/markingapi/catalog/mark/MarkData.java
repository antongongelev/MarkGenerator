package com.markgenerator.markingapi.catalog.mark;


import com.markgenerator.markingapi.catalog.MarkType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Разобранная [акцизная] марка марикрованного продукта
 */
public class MarkData {

    /**
     * Тип маркированного товара
     */
    private final MarkType markType;

    /**
     * Полная исходная (нормализованная, без префиксов и роазделителей GS1 ШК) [акцизная] марка из которой получилась эта разобранная
     */
    private final String rawMark;

    /**
     * EAN13 или EAN8 без ведущих нулей
     */
    private final String ean;

    /**
     * Серийный номер марки
     */
    private final String serialNumber;

    /**
     * МРЦ из марки (например, для табака)
     */
    private final Long minimalRetailPrice;

    /**
     * Код ТН ВЭД ЕАС (Код товарной номенклатуры внешнеэкономической деятельности Евразийского экономического союза)
     */
    private final String foreignTradeCode;

    /**
     * Исходная МРЦ из марки (без проверки и декодирования)
     */
    private final String rawMinimalRetailPrice;

    /**
     * Cрок годности
     */
    private final String shelfLife;

    /**
     * Ключ проверки
     */
    private final String verificationKey;

    /**
     * Криптоподпись
     */
    private final String verificationCode;

    /**
     * Парсер, которым была разобрана марка
     */
    private final MarkParser parser;

    public MarkData(String rawMark,
                    MarkType markType,
                    String ean,
                    String serialNumber,
                    Long minimalRetailPrice,
                    String foreignTradeCode,
                    String rawMinimalRetailPrice,
                    String shelfLife,
                    String verificationKey,
                    String verificationCode,
                    MarkParser parser) {
        Objects.requireNonNull(rawMark, "rawMark should not be null");
        Objects.requireNonNull(markType, "markType should not be null");
        Objects.requireNonNull(ean, "ean should not be null");
        Objects.requireNonNull(serialNumber, "serialNumber should not be null");
        this.rawMark = rawMark;
        this.markType = markType;
        this.ean = ean;
        this.serialNumber = serialNumber;
        this.minimalRetailPrice = minimalRetailPrice;
        this.foreignTradeCode = foreignTradeCode;
        this.rawMinimalRetailPrice = rawMinimalRetailPrice;
        this.shelfLife = shelfLife;
        this.verificationKey = verificationKey;
        this.verificationCode = verificationCode;
        this.parser = parser;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public String getEan() {
        return ean;
    }

    public String getGtin() {
        return transformToGtin(ean);
    }

    public static String transformToGtin(String ean) {
        return StringUtils.leftPad(ean, 14, '0');
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getRawMark() {
        return rawMark;
    }

    public Long getMinimalRetailPrice() {
        return minimalRetailPrice;
    }

    public String getForeignTradeCode() {
        return foreignTradeCode;
    }

    public String getRawMinimalRetailPrice() {
        return rawMinimalRetailPrice;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public MarkParser getParser() {
        return parser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkData markData = (MarkData) o;
        return markType == markData.markType &&
            rawMark.equals(markData.rawMark) &&
            ean.equals(markData.ean) &&
            serialNumber.equals(markData.serialNumber) &&
            Objects.equals(minimalRetailPrice, markData.minimalRetailPrice) &&
            Objects.equals(foreignTradeCode, markData.foreignTradeCode) &&
            Objects.equals(rawMinimalRetailPrice, markData.rawMinimalRetailPrice) &&
            Objects.equals(shelfLife, markData.shelfLife) &&
            Objects.equals(verificationKey, markData.verificationKey) &&
            Objects.equals(verificationCode, markData.verificationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(markType, rawMark, ean, serialNumber, minimalRetailPrice, foreignTradeCode, rawMinimalRetailPrice, shelfLife,
            verificationKey, verificationCode);
    }

    @Override
    public String toString() {
        return "MarkData{" +
            "markType=" + markType +
            ", rawMark='" + rawMark + '\'' +
            ", ean='" + ean + '\'' +
            ", serialNumber='" + serialNumber + '\'' +
            ", minimalRetailPrice=" + minimalRetailPrice +
            ", foreignTradeCode='" + foreignTradeCode + '\'' +
            ", rawMinimalRetailPrice='" + rawMinimalRetailPrice + '\'' +
            ", shelfLife='" + shelfLife + '\'' +
            ", verificationKey='" + verificationKey + '\'' +
            ", verificationCode='" + verificationCode + '\'' +
            '}';
    }

    public static Builder newBuilder() {
        return Builder.newBuilder();
    }

    public static final class Builder {
        private MarkType markType;
        private String rawMark;
        private String ean;
        private String serialNumber;
        private Long minimalRetailPrice;
        private String foreignTradeCode;
        private String rawMinimalRetailPrice;
        private String shelfLife;
        private String verificationKey;
        private String verificationCode;
        private MarkParser parser;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder markType(MarkType markType) {
            this.markType = markType;
            return this;
        }

        public Builder rawMark(String rawMark) {
            this.rawMark = rawMark;
            return this;
        }

        public Builder ean(String ean) {
            this.ean = ean;
            return this;
        }

        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder minimalRetailPrice(Long minimalRetailPrice) {
            this.minimalRetailPrice = minimalRetailPrice;
            return this;
        }

        public Builder foreignTradeCode(String foreignTradeCode) {
            this.foreignTradeCode = foreignTradeCode;
            return this;
        }

        public Builder rawMinimalRetailPrice(String rawMinimalRetailPrice) {
            this.rawMinimalRetailPrice = rawMinimalRetailPrice;
            return this;
        }

        public Builder shelfLife(String shelfLife) {
            this.shelfLife = shelfLife;
            return this;
        }

        public Builder verificationKey(String verificationKey) {
            this.verificationKey = verificationKey;
            return this;
        }

        public Builder verificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
            return this;
        }

        public Builder parser(MarkParser parser) {
            this.parser = parser;
            return this;
        }

        public MarkData build() {
            return new MarkData(rawMark, markType, ean, serialNumber,
                minimalRetailPrice, foreignTradeCode,
                rawMinimalRetailPrice, shelfLife,
                verificationKey, verificationCode,
                parser);
        }
    }
}
