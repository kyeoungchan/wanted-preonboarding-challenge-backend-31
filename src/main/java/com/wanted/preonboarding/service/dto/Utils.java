package com.wanted.preonboarding.service.dto;

import org.springframework.data.domain.Sort;

public class Utils {

    // snake_case -> camelCase 변환 메소드
    static String convertToCamelCase(String snakeCase) {
        if (snakeCase.contains("_")) {
            StringBuilder result = new StringBuilder();
            boolean captitalize = false;

            for (char c : snakeCase.toCharArray()) {
                if (c == '_') {
                    captitalize = true;
                } else if (captitalize) {
                    result.append(Character.toUpperCase(c));
                    captitalize = false;
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }
        return snakeCase; // 이미 camelCase인 경우 그대로 반환
    }

    static Sort createBasicSortBySortParams(String sort) {
        String[] sortParams = sort.split(":");
        String sortField = sortParams[0].equals("_score") ? sortParams[0] : Utils.convertToCamelCase(sortParams[0]); // snake_case -> camelCase 변환
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equals("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortField);
    }
}
