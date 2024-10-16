package com.team2.fitinside.banner.controller;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<BannerResponseDTO> createBanner(@RequestBody BannerRequestDTO requestDTO) {
        BannerResponseDTO responseDTO = bannerService.createBanner(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BannerResponseDTO>> getAllBanners() {
        List<BannerResponseDTO> responses = bannerService.getAllBanners();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerResponseDTO> updateBanner(
            @PathVariable Long id, @RequestBody BannerRequestDTO requestDTO) {
        BannerResponseDTO responseDTO = bannerService.updateBanner(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}

