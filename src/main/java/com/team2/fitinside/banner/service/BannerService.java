package com.team2.fitinside.banner.service;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import com.team2.fitinside.banner.mapper.BannerMapper;
import com.team2.fitinside.banner.repository.BannerRepository;
import com.team2.fitinside.product.image.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper = BannerMapper.INSTANCE;
    private final S3ImageService s3ImageService;

    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);

        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
                .title(title)
                .displayOrder(displayOrder)
                .imageUrl(imageUrl) // S3에서 받은 이미지 URL을 DTO에 설정
                .build();

        Banner banner = bannerMapper.toEntity(requestDTO);
        bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    public BannerResponseDTO updateBanner(Long id, String title, Integer displayOrder, MultipartFile image) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }

        String newImageUrl = s3ImageService.upload(image);

        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
                .title(title)
                .displayOrder(displayOrder)
                .imageUrl(newImageUrl) // S3에서 받은 새 이미지 URL을 DTO에 설정
                .build();

        banner.updateDetails(requestDTO.getTitle(), requestDTO.getImageUrl(), requestDTO.getDisplayOrder());
        bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    @Transactional(readOnly = true)
    public List<BannerResponseDTO> getAllBanners() {
        return bannerRepository.findByIsDeletedFalse().stream()
                .map(bannerMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }
        banner.delete();
        bannerRepository.save(banner);
    }
}


