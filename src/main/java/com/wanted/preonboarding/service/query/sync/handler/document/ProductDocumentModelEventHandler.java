package com.wanted.preonboarding.service.query.sync.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.service.query.sync.handler.AbstractCdcEventHandler;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ProductDocumentModelEventHandler extends AbstractCdcEventHandler {
    public ProductDocumentModelEventHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    protected LocalDateTime parseTimestampToLocalDateTime(Object timestamp) {
        if (timestamp == null) {
            return null;
        }

        if (timestamp instanceof String) {
            try {
                // PostgreSQL timestamp 형식 파싱
                return LocalDateTime.parse((String) timestamp);
            } catch (Exception e) {
                try {
                    // 다른 ISO 형식이나 특수 형식 파싱 시도
                    return LocalDateTime.parse((String) timestamp, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e2) {
                    log.warn("Failed to parse timestamp {}", timestamp, e2);
                    return null;
                }
            }
        } else if (timestamp instanceof Number) {
            try {
                // 마이크로초 단위를 밀리초로 변환 (1/1000)
                long microseconds = ((Number) timestamp).longValue();
                long milliseconds = microseconds / 1000;

                return LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(milliseconds),
                        ZoneId.systemDefault()
                );
            } catch (Exception e) {
                log.warn("Failed to parse timestamp number: {}", timestamp, e);
                return null;
            }
        }

        return null;
    }
}
