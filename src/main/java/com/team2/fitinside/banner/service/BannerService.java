package com.team2.fitinside.banner.service;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import com.team2.fitinside.banner.mapper.BannerMapper;
import com.team2.fitinside.banner.repository.BannerRepository;
import com.team2.fitinside.product.image.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


//@Service
//@RequiredArgsConstructor
//@Transactional
//public class BannerService {
//
//    private final BannerRepository bannerRepository;
//    private final BannerMapper bannerMapper = BannerMapper.INSTANCE;
//    private final S3ImageService s3ImageService;
//
//    // 배너 생성 로직
//    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image) {
//        String imageUrl = s3ImageService.upload(image);
//
//        // displayOrder가 주어진 경우, 그 이후의 배너들 displayOrder + 1
//        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThanEqual(displayOrder);
//        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1)); // updateDisplayOrder 사용
//        bannerRepository.saveAll(bannersToUpdate);
//
//        // 배너 생성
//        BannerRequestDTO requestDTO = BannerRequestDTO.builder()
//                .title(title)
//                .displayOrder(displayOrder)
//                .imageUrl(imageUrl)
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
//        // 이미지가 있는 경우만 업데이트, 그렇지 않으면 기존 이미지 유지
//        if (image != null && !image.isEmpty()) {
//            if (banner.getImageUrl() != null) {
//                s3ImageService.deleteImageFromS3(banner.getImageUrl());
//            }
//            String newImageUrl = s3ImageService.upload(image);
//            banner.updateDetails(title, newImageUrl, displayOrder);
//        } else {
//            // 이미지가 없는 경우 제목과 표시 순서만 업데이트
//            banner.updateDetails(title, banner.getImageUrl(), displayOrder);
//        }
//
//        bannerRepository.save(banner);
//        return bannerMapper.toDto(banner);
//    }
//
//
//    // 배너 삭제 로직
//    public void deleteBanner(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//
//        Integer currentDisplayOrder = banner.getDisplayOrder();
//
//        // 배너 삭제 후, 그 뒤의 배너들을 한 칸씩 앞으로 이동
//        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThan(currentDisplayOrder);
//        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1)); // updateDisplayOrder 사용
//        bannerRepository.saveAll(bannersToUpdate);
//
//        // 이미지 삭제 및 배너 삭제
//        if (banner.getImageUrl() != null) {
//            s3ImageService.deleteImageFromS3(banner.getImageUrl());
//        }
//        bannerRepository.delete(banner);
//    }
//
//    // 모든 배너 가져오기 (displayOrder로 정렬)
//    @Transactional(readOnly = true)
//    public List<BannerResponseDTO> getAllBanners() {
//        return bannerRepository.findByIsDeletedFalse().stream()
//                .sorted(Comparator.comparingInt(Banner::getDisplayOrder)) // displayOrder로 정렬
//                .map(bannerMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    // ID로 배너 하나 가져오기
//    @Transactional(readOnly = true)
//    public BannerResponseDTO getBannerById(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//        return bannerMapper.toDto(banner);
//    }
//}

//@Service
//@RequiredArgsConstructor
//@Transactional
//public class BannerService {
//
//    private final BannerRepository bannerRepository;
//    private final S3ImageService s3ImageService;
//
//    // 배너 생성 로직
//    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image, String targetUrl) {
//        String imageUrl = s3ImageService.upload(image);
//
//        // 최대 displayOrder 계산 (배너가 없을 경우 size + 1)
//        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size() + 1;
//
//        // 새로운 displayOrder가 최대값을 넘지 않도록 조정
//        if (displayOrder > maxDisplayOrder) {
//            displayOrder = (int) maxDisplayOrder;
//        }
//
//        // displayOrder 이후 배너들 displayOrder + 1
//        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThanEqual(displayOrder);
//        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1)); // displayOrder 밀어내기
//        bannerRepository.saveAll(bannersToUpdate);
//
//        // 배너 생성
//        Banner banner = Banner.builder()
//                .title(title)
//                .displayOrder(displayOrder)
//                .imageUrl(imageUrl)
//                .targetUrl(targetUrl) // targetUrl 추가
//                .isDeleted(false)
//                .build();
//
//        bannerRepository.save(banner);
//
//        return toResponseDTO(banner);
//    }
//
//    // 배너 수정 로직
//    public BannerResponseDTO updateBanner(Long id, String title, Integer newDisplayOrder, MultipartFile image, String targetUrl) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//
//        // 기존 displayOrder 저장
//        Integer oldDisplayOrder = banner.getDisplayOrder();
//
//        // 최대 displayOrder 계산
//        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size();
//        if (newDisplayOrder > maxDisplayOrder) {
//            newDisplayOrder = (int) maxDisplayOrder; // 새로운 displayOrder가 최대값을 넘지 않도록 조정
//        }
//
//        // 새로운 displayOrder와 기존 displayOrder 비교
//        if (!oldDisplayOrder.equals(newDisplayOrder)) {
//            adjustDisplayOrder(oldDisplayOrder, newDisplayOrder);
//        }
//
//        String imageUrl = banner.getImageUrl();
//
//        // 이미지 업데이트
//        if (image != null && !image.isEmpty()) {
//            if (banner.getImageUrl() != null) {
//                s3ImageService.deleteImageFromS3(banner.getImageUrl());
//            }
//            imageUrl = s3ImageService.upload(image);
//        }
//
//        // 배너를 새로 빌더 패턴으로 생성 (기존 배너 객체를 업데이트하는 대신)
//        Banner updatedBanner = Banner.builder()
//                .id(banner.getId())
//                .title(title)
//                .displayOrder(newDisplayOrder)
//                .imageUrl(imageUrl)
//                .targetUrl(targetUrl) // targetUrl 추가
//                .isDeleted(banner.isDeleted())
//                .build();
//
//        bannerRepository.save(updatedBanner);
//        return toResponseDTO(updatedBanner);
//    }
//
//    // 배너 삭제 로직
//    public void deleteBanner(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//
//        Integer currentDisplayOrder = banner.getDisplayOrder();
//
//        // 배너 삭제 후 그 뒤의 배너들 displayOrder - 1
//        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThan(currentDisplayOrder);
//        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1)); // displayOrder 앞으로 당기기
//        bannerRepository.saveAll(bannersToUpdate);
//
//        // 이미지 삭제 및 배너 삭제
//        if (banner.getImageUrl() != null) {
//            s3ImageService.deleteImageFromS3(banner.getImageUrl());
//        }
//        bannerRepository.delete(banner);
//    }
//
//    // displayOrder 조정 로직
//    private void adjustDisplayOrder(Integer oldOrder, Integer newOrder) {
//        if (newOrder > oldOrder) {
//            // oldOrder 뒤에 있는 배너들을 한 칸씩 앞으로 이동
//            List<Banner> bannersToMoveUp = bannerRepository.findByDisplayOrderBetween(oldOrder + 1, newOrder);
//            bannersToMoveUp.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1));
//            bannerRepository.saveAll(bannersToMoveUp);
//        } else if (newOrder < oldOrder) {
//            // oldOrder 앞에 있는 배너들을 한 칸씩 뒤로 이동
//            List<Banner> bannersToMoveDown = bannerRepository.findByDisplayOrderBetween(newOrder, oldOrder - 1);
//            bannersToMoveDown.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1));
//            bannerRepository.saveAll(bannersToMoveDown);
//        }
//    }
//
//    // 모든 배너 가져오기 (displayOrder로 정렬)
//    @Transactional(readOnly = true)
//    public List<BannerResponseDTO> getAllBanners() {
//        return bannerRepository.findByIsDeletedFalse().stream()
//                .sorted(Comparator.comparingInt(Banner::getDisplayOrder)) // displayOrder로 정렬
//                .map(this::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    // ID로 배너 하나 가져오기
//    @Transactional(readOnly = true)
//    public BannerResponseDTO getBannerById(Long id) {
//        Banner banner = bannerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
//        return toResponseDTO(banner);
//    }
//
//    // DTO 변환 메서드
//    private BannerResponseDTO toResponseDTO(Banner banner) {
//        return BannerResponseDTO.builder()
//                .id(banner.getId())
//                .title(banner.getTitle())
//                .displayOrder(banner.getDisplayOrder())
//                .imageUrl(banner.getImageUrl())
//                .targetUrl(banner.getTargetUrl()) // targetUrl 필드 추가
//                .build();
//    }
//}


@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final S3ImageService s3ImageService;

    // 배너 생성 로직
    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image, String targetUrl) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3ImageService.upload(image);
        }

        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size() + 1;

        if (displayOrder > maxDisplayOrder) {
            displayOrder = (int) maxDisplayOrder;
        }

        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThanEqual(displayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1));
        bannerRepository.saveAll(bannersToUpdate);

        Banner banner = Banner.builder()
                .title(title)
                .displayOrder(displayOrder)
                .imageUrl(imageUrl)
                .targetUrl(targetUrl != null ? targetUrl : "")
                .isDeleted(false)
                .build();

        bannerRepository.save(banner);
        return toResponseDTO(banner);
    }

    // 배너 수정 로직
    public BannerResponseDTO updateBanner(Long id, String title, Integer newDisplayOrder, MultipartFile image, String targetUrl) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer oldDisplayOrder = banner.getDisplayOrder();
        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size();
        if (newDisplayOrder > maxDisplayOrder) {
            newDisplayOrder = (int) maxDisplayOrder;
        }

        if (!oldDisplayOrder.equals(newDisplayOrder)) {
            adjustDisplayOrder(oldDisplayOrder, newDisplayOrder);
        }

        String imageUrl = banner.getImageUrl();

        if (image != null && !image.isEmpty()) {
            if (banner.getImageUrl() != null) {
                s3ImageService.deleteImageFromS3(banner.getImageUrl());
            }
            imageUrl = s3ImageService.upload(image);
        }

        String updatedTargetUrl = targetUrl != null ? targetUrl : banner.getTargetUrl();

        Banner updatedBanner = Banner.builder()
                .id(banner.getId())
                .title(title)
                .displayOrder(newDisplayOrder)
                .imageUrl(imageUrl)
                .targetUrl(updatedTargetUrl)
                .isDeleted(banner.isDeleted())
                .build();

        bannerRepository.save(updatedBanner);
        return toResponseDTO(updatedBanner);
    }

    // 배너 삭제 로직
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer currentDisplayOrder = banner.getDisplayOrder();

        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThan(currentDisplayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1));
        bannerRepository.saveAll(bannersToUpdate);

        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }
        bannerRepository.delete(banner);
    }

    private void adjustDisplayOrder(Integer oldOrder, Integer newOrder) {
        if (newOrder > oldOrder) {
            List<Banner> bannersToMoveUp = bannerRepository.findByDisplayOrderBetween(oldOrder + 1, newOrder);
            bannersToMoveUp.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1));
            bannerRepository.saveAll(bannersToMoveUp);
        } else if (newOrder < oldOrder) {
            List<Banner> bannersToMoveDown = bannerRepository.findByDisplayOrderBetween(newOrder, oldOrder - 1);
            bannersToMoveDown.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1));
            bannerRepository.saveAll(bannersToMoveDown);
        }
    }

    @Transactional(readOnly = true)
    public List<BannerResponseDTO> getAllBanners() {
        return bannerRepository.findByIsDeletedFalse().stream()
                .sorted(Comparator.comparingInt(Banner::getDisplayOrder))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BannerResponseDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
        return toResponseDTO(banner);
    }

    private BannerResponseDTO toResponseDTO(Banner banner) {
        return BannerResponseDTO.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .displayOrder(banner.getDisplayOrder())
                .imageUrl(banner.getImageUrl() != null ? banner.getImageUrl() : "")
                .targetUrl(banner.getTargetUrl() != null ? banner.getTargetUrl() : "")
                .build();
    }
}
