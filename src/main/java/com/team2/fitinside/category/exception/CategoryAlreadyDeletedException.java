package com.team2.fitinside.category.exception;

import com.team2.fitinside.global.exception.exception.CustomException;
import com.team2.fitinside.global.exception.exception.ErrorCode;

public class CategoryAlreadyDeletedException extends CustomException {
    public CategoryAlreadyDeletedException() {
        super(ErrorCode.CATEGORY_ALREADY_DELETED); // ErrorCode에서 CATEGORY_ALREADY_DELETED 참조
    }
}
