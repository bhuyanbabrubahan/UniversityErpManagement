package com.sms.util;

import com.sms.exception.BusinessException;

public final class PageValidator {

    private PageValidator() {
    }

    public static void validate(
            int page,
            int size
    ) {

        if (page < 0) {

            throw new BusinessException(
                    "Page number cannot be negative."
            );
        }

        if (size <= 0) {

            throw new BusinessException(
                    "Page size must be greater than zero."
            );
        }

        if (size > 100) {

            throw new BusinessException(
                    "Maximum page size allowed is 100."
            );
        }
    }

}