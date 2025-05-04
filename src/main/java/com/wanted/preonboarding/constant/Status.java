package com.wanted.preonboarding.constant;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ON_SALE("판매중"), SOLD_OUT("품절"), DELETED("삭제됨");

    private final String status;

    public static Status getInstance(String status) {
        return Arrays.stream(values())
                .filter(s -> s.getStatus().equals(status))
                .findAny()
                .orElse(null);
    }
}
