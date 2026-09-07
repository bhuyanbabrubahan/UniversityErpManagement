package com.sms.util;

import java.util.Set;

import com.sms.exception.BusinessException;

public final class SortFieldValidator {

    private SortFieldValidator() {
    }

    public static void validate(
            String sortBy,
            Set<String> allowedFields
    ) {

        if (!allowedFields.contains(sortBy)) {

            throw new BusinessException(
                    "Invalid sort field : " + sortBy
            );
        }
    }

}