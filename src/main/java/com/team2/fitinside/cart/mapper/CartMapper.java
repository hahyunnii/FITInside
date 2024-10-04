package com.team2.fitinside.cart.mapper;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseDto;
import com.team2.fitinside.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "user", ignore = true) // user 필드 매핑 제외
    @Mapping(target = "product", ignore = true) // product 필드 매핑 제외
    Cart toEntity(CartCreateRequestDto cartCreateRequestDto);

//    Cart toEntity();

    @Mapping(target = "productName", ignore = true) // 상품 이름 필드 매핑 제외
    @Mapping(target = "productPrice", ignore = true) // 상품 가격 필드 매핑 제외
    CartResponseDto toCartResponseDto(Cart cart);
}
