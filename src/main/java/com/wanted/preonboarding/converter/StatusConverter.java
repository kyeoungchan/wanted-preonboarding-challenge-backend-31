package com.wanted.preonboarding.converter;

import com.wanted.preonboarding.constant.Status;
import jakarta.persistence.AttributeConverter;

public class StatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        return Status.getInstance(s);
    }
}
