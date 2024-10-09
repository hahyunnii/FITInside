package com.team2.fitinside.category.exception;

import com.team2.fitinside.global.exception.exception.CustomException;
import com.team2.fitinside.global.exception.exception.ErrorCode;

public class CategoryNotFoundException extends CustomException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND); // ErrorCode에서 정의된 CATEGORY_NOT_FOUND 참조
    }
}
