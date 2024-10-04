package com.team2.fitinside.product.mapper;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductCreateDto productCreateDto);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductUpdateDto productUpdateDto);

    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDto toDto(Product product);
}
