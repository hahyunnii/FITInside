package com.team2.fitinside.cart.service;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.cart.exception.CartDuplicatedException;
import com.team2.fitinside.cart.exception.CartOutOfRangeException;
import com.team2.fitinside.cart.mapper.CartMapper;
import com.team2.fitinside.cart.repository.CartRepository;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.product.repository.ProductRepository;
import com.team2.fitinside.user.entity.User;
import com.team2.fitinside.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 장바구니 조회 메서드
    public CartResponseWrapperDto findAllCarts() throws AccessDeniedException {

        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();

        List<CartResponseDto> dtos = new ArrayList<>();

        // 장바구니 목록 조회
        List<Cart> cartList = cartRepository.findAllByUser_Email(email);

        // cart -> List<CartResponseDto>
        for (Cart cart : cartList) {
            CartResponseDto cartResponseDto = CartMapper.INSTANCE.toCartResponseDto(cart);
            cartResponseDto.setProductName(cart.getProduct().getProductName());
            cartResponseDto.setProductPrice(cart.getProduct().getPrice());
            dtos.add(cartResponseDto);
        }

        // 성공메시지 + List<CartResponseDto> -> CartResponseWrapperDto 반환
        return new CartResponseWrapperDto("장바구니 조회 완료했습니다!", dtos);
    }

    // 장바구니 생성 메서드
    @Transactional
    public void createCart(CartCreateRequestDto dto) throws AccessDeniedException {

        // 수량 범위 체크 메서드 호출
        checkQuantity(dto.getQuantity());

        Cart cart = CartMapper.INSTANCE.toEntity(dto);

        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();

        // user 찾음
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다!"));

        // product 찾음
        Product findProduct = productRepository.findById(dto.getProductId()).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다!"));

        // 이미 같은 장바구니가 있다면 예외 발생
        if(cartRepository.existsCartByUser_IdAndProduct_Id(findUser.getId(), findProduct.getId())) {
            throw new CartDuplicatedException("이미 존재하는 장바구니입니다!");
        }

        // cart에 연관관계 설정
        cart.setUserAndProduct(findUser, findProduct);

        cartRepository.save(cart);
    }

    // 장바구니 수정 메서드
    @Transactional
    public void updateCart(CartUpdateRequestDto dto) throws AccessDeniedException{
        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();

        // 수량 범위 체크 메서드 호출
        checkQuantity(dto.getQuantity());

        // cartId로 cart 조회
        Cart cart = cartRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다."));

        // 요청한 회원과 장바구니에 저장된 회원정보가 다르면 예외 반환
        if(!email.equals(cart.getUser().getEmail())) {
            throw new AccessDeniedException("권한이 없습니다!");
        }

        // 수량을 동일하게 수정하면 리턴
        if(cart.getQuantity() == dto.getQuantity()) return;

        cart.updateQuantity(dto.getQuantity());
    }

    // 장바구니 단일 삭제 메서드
    @Transactional
    public void deleteCart(Long cartId) throws AccessDeniedException {
        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();

        // cartId로 cart 조회
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다!"));

        // 요청한 회원과 장바구니에 저장된 회원정보가 다르면 예외 반환
        if(!email.equals(cart.getUser().getEmail())) {
            throw new AccessDeniedException("권한이 없습니다!");
        }

        // 장바구니 단일 삭제
        cartRepository.delete(cart);
    }

    // 장바구니 단일 삭제 메서드
    @Transactional
    public void clearCart() throws AccessDeniedException {

        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();

        // user의 email로 장바구니 모두 가져옴
        List<Cart> cartList = cartRepository.findAllByUser_Email(email);

        // 장바구니 리스트 전체 삭제
        cartRepository.deleteAll(cartList);

    }

    // 수정범위 1~20 넘어가면 CartOutOfRangeException 던짐
    static void checkQuantity(int quantity) {
        if(quantity < 1 || quantity > 20) {
            throw new CartOutOfRangeException("상품 수량은 1개 이상 20개 이하여야 합니다!");
        }
    }

    // TODO: CustomUserDetails 확인해야함!
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

    private String getAuthenticatedUserEmail(){
        return "person1@example.com";
    }

}
