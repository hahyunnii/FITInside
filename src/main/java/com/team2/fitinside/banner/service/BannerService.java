package com.team2.fitinside.banner.service;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import com.team2.fitinside.banner.mapper.BannerMapper;
import com.team2.fitinside.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper = BannerMapper.INSTANCE;

    public BannerResponseDTO createBanner(BannerRequestDTO requestDTO) {
        Banner banner = bannerMapper.toEntity(requestDTO);
        bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    @Transactional(readOnly = true)
    public List<BannerResponseDTO> getAllBanners() {
        return bannerRepository.findByIsDeletedFalse().stream()
                .map(bannerMapper::toDto)
                .collect(Collectors.toList());
    }

    public BannerResponseDTO updateBanner(Long id, BannerRequestDTO requestDTO) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
        banner = bannerMapper.toEntity(requestDTO);
        bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
        banner.setIsDeleted();
        bannerRepository.save(banner);
    }
}

