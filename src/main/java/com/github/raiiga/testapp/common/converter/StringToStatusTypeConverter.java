package com.github.raiiga.testapp.common.converter;

import com.github.raiiga.testapp.dto.type.CompanyStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusTypeConverter implements Converter<String, CompanyStatus> {

    @Override
    public CompanyStatus convert(String source) {
        return CompanyStatus.valueOf(source.toUpperCase());
    }
}