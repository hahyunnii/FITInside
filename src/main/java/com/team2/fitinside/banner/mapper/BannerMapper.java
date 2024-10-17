package com.team2.fitinside.banner.mapper;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BannerMapper {
    BannerMapper INSTANCE = Mappers.getMapper(BannerMapper.class);

    Banner toEntity(BannerRequestDTO requestDTO);

    BannerResponseDTO toDto(Banner banner);
}
