package com.team2.fitinside.category.exception;

import com.team2.fitinside.global.exception.exception.CustomException;
import com.team2.fitinside.global.exception.exception.ErrorCode;

public class CategoryResponseNotFoundException extends CustomException {
    public CategoryResponseNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND); // ErrorCode에서 CATEGORY_NOT_FOUND 사용
    }
}
