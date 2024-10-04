package com.team2.fitinside.product.mapper;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductImgDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.entity.ProductImg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // categoryId는 Product 엔티티의 필드이므로 별도의 복잡한 매핑이 필요하지 않음
    Product toEntity(ProductCreateDto productCreateDto);

    Product toEntity(ProductUpdateDto productUpdateDto);

    // 기존 엔티티에 업데이트 정보를 반영하는 메서드
    void updateEntityFromDto(ProductUpdateDto productUpdateDto, @MappingTarget Product product);

    ProductResponseDto toDto(Product product);

    // ProductImg 엔티티 -> ProductImgDto 변환
    ProductImgDto toProductImgDto(ProductImg productImg);

    // ProductImgDto -> ProductImg 엔티티 변환
    ProductImg toProductImg(ProductImgDto productImgDto);
}
