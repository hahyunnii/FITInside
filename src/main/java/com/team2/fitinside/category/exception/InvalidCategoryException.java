package com.team2.fitinside.category.exception;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;

public class InvalidCategoryException extends CustomException {
    public InvalidCategoryException() {
        super(ErrorCode.INVALID_CATEGORY_DATA); // ErrorCode에서 카테고리 관련 오류 코드를 참조
    }
}
