package com.team2.fitinside.category.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long displayOrder;
    private Boolean isDeleted;
    private Long parentId;  // 부모 카테고리 id만 참조
}
