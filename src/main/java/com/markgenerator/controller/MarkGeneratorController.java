package com.markgenerator.controller;

import com.markgenerator.dto.MarkRequest;
import com.markgenerator.markingapi.catalog.mark.MarkParserCountry;
import com.markgenerator.service.MarkGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "generate/")
public class MarkGeneratorController {

    @Autowired
    private MarkGeneratorService markGenerator;

    @PostMapping()
    public String generateByMarkType(@RequestBody MarkRequest markRequest) {
        return markGenerator.generateByMarkType(markRequest);
    }

    @GetMapping("countries/")
    public List<String> getCountries() {
        return markGenerator.getCountries();
    }

    @GetMapping("{country}/markTypes/")
    public List<String> getMarkTypesByCountry(@PathVariable MarkParserCountry country) {
        return markGenerator.getMarkTypesByCountry(country);
    }
}
