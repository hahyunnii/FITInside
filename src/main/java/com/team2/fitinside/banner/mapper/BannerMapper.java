package com.team2.fitinside.banner.mapper;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BannerMapper {
    BannerMapper INSTANCE = Mappers.getMapper(BannerMapper.class);

    // DTO -> Entity 변환
    @Mapping(target = "targetUrl", source = "targetUrl") // URL 필드 추가
    Banner toEntity(BannerRequestDTO requestDTO);

    // Entity -> ResponseDTO 변환
    @Mapping(target = "targetUrl", source = "targetUrl") // URL 필드 추가
    BannerResponseDTO toDto(Banner banner);
}


