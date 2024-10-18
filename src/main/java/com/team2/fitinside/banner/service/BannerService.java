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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//@Transactional
//public class BannerService {
//
//    private final BannerRepository bannerRepository;
//    private final BannerMapper bannerMapper = BannerMapper.INSTANCE;
//    private final S3ImageService s3ImageService;
//
//    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image) {
//        String imageUrl = s3ImageService.upload(image);
//
//        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
//                .title(title)
//                .displayOrder(displayOrder)
//                .imageUrl(imageUrl) // S3에서 받은 이미지 URL을 DTO에 설정
//                .build();
//
//        Banner banner = bannerMapper.toEntity(requestDTO);
//        bannerRepository.save(banner);
//        return bannerMapper.toDto(banner);
//    }
//
//    public BannerResponseDTO updateBanner(Long id, String title, Integer displayOrder, MultipartFile image) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//
//        if (banner.getImageUrl() != null) {
//            s3ImageService.deleteImageFromS3(banner.getImageUrl());
//        }
//
//        String newImageUrl = s3ImageService.upload(image);
//
//        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
//                .title(title)
//                .displayOrder(displayOrder)
//                .imageUrl(newImageUrl) // S3에서 받은 새 이미지 URL을 DTO에 설정
//                .build();
//
//        banner.updateDetails(requestDTO.getTitle(), requestDTO.getImageUrl(), requestDTO.getDisplayOrder());
//        bannerRepository.save(banner);
//        return bannerMapper.toDto(banner);
//    }
//
//    @Transactional(readOnly = true)
//    public List<BannerResponseDTO> getAllBanners() {
//        return bannerRepository.findByIsDeletedFalse().stream()
//                .map(bannerMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ID로 배너 하나를 가져오는 메서드
//    @Transactional(readOnly = true)
//    public BannerResponseDTO getBannerById(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//        return bannerMapper.toDto(banner);
//    }
//
//    public void deleteBanner(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//
//        if (banner.getImageUrl() != null) {
//            s3ImageService.deleteImageFromS3(banner.getImageUrl());
//        }
//        banner.delete();
//        bannerRepository.save(banner);
//    }
//}
//
//


@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper = BannerMapper.INSTANCE;
    private final S3ImageService s3ImageService;

    // 배너 생성 로직
    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);

        // displayOrder가 주어진 경우, 그 이후의 배너들 displayOrder + 1
        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThanEqual(displayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1)); // updateDisplayOrder 사용
        bannerRepository.saveAll(bannersToUpdate);

        // 배너 생성
        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
                .title(title)
                .displayOrder(displayOrder)
                .imageUrl(imageUrl)
                .build();

        Banner banner = bannerMapper.toEntity(requestDTO);
        bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    // 배너 수정 로직
    public BannerResponseDTO updateBanner(Long id, String title, Integer newDisplayOrder, MultipartFile image) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer currentDisplayOrder = banner.getDisplayOrder();

        // 기존 displayOrder와 새로운 displayOrder를 비교하여 순서 조정
        if (newDisplayOrder > currentDisplayOrder) {
            List<Banner> bannersToMoveUp = bannerRepository.findByDisplayOrderBetween(currentDisplayOrder + 1, newDisplayOrder);
            bannersToMoveUp.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1)); // updateDisplayOrder 사용
            bannerRepository.saveAll(bannersToMoveUp);
        } else if (newDisplayOrder < currentDisplayOrder) {
            List<Banner> bannersToMoveDown = bannerRepository.findByDisplayOrderBetween(newDisplayOrder, currentDisplayOrder - 1);
            bannersToMoveDown.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1)); // updateDisplayOrder 사용
            bannerRepository.saveAll(bannersToMoveDown);
        }

        // 배너 업데이트
        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }
        String newImageUrl = s3ImageService.upload(image);

        banner.updateDetails(title, newImageUrl, newDisplayOrder);
        bannerRepository.save(banner);

        return bannerMapper.toDto(banner);
    }

    // 배너 삭제 로직
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer currentDisplayOrder = banner.getDisplayOrder();

        // 배너 삭제 후, 그 뒤의 배너들을 한 칸씩 앞으로 이동
        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThan(currentDisplayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1)); // updateDisplayOrder 사용
        bannerRepository.saveAll(bannersToUpdate);

        // 이미지 삭제 및 배너 삭제
        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }
        bannerRepository.delete(banner);
    }

    // 모든 배너 가져오기 (displayOrder로 정렬)
    @Transactional(readOnly = true)
    public List<BannerResponseDTO> getAllBanners() {
        return bannerRepository.findByIsDeletedFalse().stream()
                .sorted(Comparator.comparingInt(Banner::getDisplayOrder)) // displayOrder로 정렬
                .map(bannerMapper::toDto)
                .collect(Collectors.toList());
    }

    // ID로 배너 하나 가져오기
    @Transactional(readOnly = true)
    public BannerResponseDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
        return bannerMapper.toDto(banner);
    }
}


