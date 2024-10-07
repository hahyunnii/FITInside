package com.team2.fitinside.category.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long displayOrder;
    private Boolean isDeleted;
    private Long parentId;  // 부모 카테고리 id만 참조
}
