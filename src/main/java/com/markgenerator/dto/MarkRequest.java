package com.markgenerator.dto;

import com.markgenerator.markingapi.catalog.MarkType;
import com.markgenerator.markingapi.catalog.mark.MarkParserCountry;
import lombok.Data;

@Data
public class MarkRequest {

    private String barcode;
    private MarkType markType;
    private MarkParserCountry country;
}
