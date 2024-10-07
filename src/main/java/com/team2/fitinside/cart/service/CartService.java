package com.team2.fitinside.cart.service;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.exception.CartOutOfRangeException;
import com.team2.fitinside.cart.mapper.CartMapper;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 장바구니 조회 메서드
    public CartResponseWrapperDto findAllCarts() throws AccessDeniedException {

        // member의 id 가져옴 + 권한검사
        Long loginMemberID = getAuthenticatedMemberId();
        List<CartResponseDto> dtos = new ArrayList<>();
        List<Cart> cartList = cartRepository.findAllByMember_Id(loginMemberID);

        // cart -> List<CartResponseDto>
        for (Cart cart : cartList) {
            CartResponseDto cartResponseDto = CartMapper.INSTANCE.toCartResponseDto(cart);
            dtos.add(cartResponseDto);
        }

        // 성공메시지 + List<CartResponseDto> -> CartResponseWrapperDto 반환
        return new CartResponseWrapperDto("장바구니 조회 완료했습니다!", dtos);
    }

    // 장바구니 생성 메서드
    @Transactional
    public void createCart(CartCreateRequestDto dto) throws AccessDeniedException {

        checkQuantity(dto.getQuantity());

        Long loginMemberID = getAuthenticatedMemberId();

        // 이미 같은 장바구니가 있다면 수정
        if(cartRepository.existsCartByMember_IdAndProduct_Id(loginMemberID, dto.getProductId())) {
            Cart foundCart = cartRepository.findByMember_IdAndProduct_Id(loginMemberID, dto.getProductId()).orElse(null);
            Objects.requireNonNull(foundCart).updateQuantity(dto.getQuantity());
            return;
        }

        Cart cart = CartMapper.INSTANCE.toEntity(dto);
        Product foundProduct = productRepository.findById(dto.getProductId()).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다!"));
        Member foundMember = memberRepository.findById(loginMemberID).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다!"));
        cart.setUserAndProduct(foundMember, foundProduct);

        cartRepository.save(cart);
    }

    // 장바구니 수정 메서드
    @Transactional
    public void updateCart(CartUpdateRequestDto dto) throws AccessDeniedException{

        Long loginMemberID = getAuthenticatedMemberId();

        checkQuantity(dto.getQuantity());

        Cart cart = cartRepository.findByMember_IdAndProduct_Id(loginMemberID, dto.getProductId()).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다."));

        if(!loginMemberID.equals(cart.getMember().getId())) {
            throw new AccessDeniedException("권한이 없습니다!");
        }

        // 수량을 동일하게 수정하면 리턴
        if(cart.getQuantity() == dto.getQuantity()) return;
        cart.updateQuantity(dto.getQuantity());
    }

    // 장바구니 단일 삭제 메서드
    @Transactional
    public void deleteCart(Long productId) throws AccessDeniedException {

        Long loginMemberID = getAuthenticatedMemberId();

        Cart cart = cartRepository.findByMember_IdAndProduct_Id(loginMemberID, productId).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다!"));

        if(!loginMemberID.equals(cart.getMember().getId())) {
            throw new AccessDeniedException("권한이 없습니다!");
        }

        cartRepository.delete(cart);
    }

    // 장바구니 단일 삭제 메서드
    @Transactional
    public void clearCart() throws AccessDeniedException {

        Long loginMemberID = getAuthenticatedMemberId();

        List<Cart> cartList = cartRepository.findAllByMember_Id(loginMemberID);
        cartRepository.deleteAll(cartList);
    }

    // 수정범위 확인 메서드
    static void checkQuantity(int quantity) {

        if(quantity < 1 || quantity > 20) {
            throw new CartOutOfRangeException("상품 수량은 1개 이상 20개 이하여야 합니다!");
        }
    }

    // 사용자의 권환 확인 + memberId 가져오는 메서드
    // 따로 분리한 이유 : RuntimeException이 아닌 AccessDeniedException 예외 처리 위해서
    private Long getAuthenticatedMemberId() throws AccessDeniedException {
        try {
            return SecurityUtil.getCurrentMemberId();
        } catch (RuntimeException e) {
            throw new AccessDeniedException("권한이 없습니다!");
        }
    }

    // 사용자의 권환 확인 + 정보 가져오는 메서드
//    private String getAuthenticatedUserEmail() throws AccessDeniedException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
//            throw new AccessDeniedException("권한이 없습니다!");
//        }
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return userDetails.getUsername(); // getUsername()을 호출
////        return (CustomUserDetails) authentication.getPrincipal().getUserName();
//    }
}
