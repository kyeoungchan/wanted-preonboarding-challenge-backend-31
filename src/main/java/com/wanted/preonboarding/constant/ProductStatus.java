package com.wanted.preonboarding.constant;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    ACTIVE("판매중"), OUT_OF_STOCK("품절"), DELETED("삭제됨");

    private final String status;

    public static ProductStatus getInstance(String status) {
        return Arrays.stream(values())
                .filter(s -> s.getStatus().equals(status))
                .findAny()
                .orElse(null);
    }
}
