package com.team2.fitinside.category.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequestDTO {
    @NotBlank(message = "Name is required") // name 필드는 공백일 수 X
    //private Long id;
    private String name;
    private Long displayOrder;
    private Boolean isDeleted;
    private Long parentId;  // 부모 카테고리 id만 참조
}

