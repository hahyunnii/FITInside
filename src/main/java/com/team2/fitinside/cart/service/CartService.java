package com.team2.fitinside.cart.service;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseDto;
import com.team2.fitinside.cart.dto.CartResponseWrapperDto;
import com.team2.fitinside.cart.dto.CartUpdateRequestDto;
import com.team2.fitinside.cart.entity.Cart;
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
    public CartResponseWrapperDto findAllCarts() {

        // user의 email 가져옴 + 권한검사
        String email = getAuthenticatedUserEmail();
        List<CartResponseDto> dtos = new ArrayList<>();
        List<Cart> cartList = cartRepository.findAllByUser_Email(email);

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
    public void createCart(CartCreateRequestDto dto) {

        checkQuantity(dto.getQuantity());
        String email = getAuthenticatedUserEmail();
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다!"));

        // 이미 같은 장바구니가 있다면 수정
        if(cartRepository.existsCartByUser_IdAndProduct_Id(findUser.getId(), dto.getProductId())) {
            cartRepository.findByUser_IdAndProduct_Id(findUser.getId(), dto.getProductId()).updateQuantity(dto.getQuantity());
            return;
        }

        Cart cart = CartMapper.INSTANCE.toEntity(dto);
        Product findProduct = productRepository.findById(dto.getProductId()).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다!"));
        cart.setUserAndProduct(findUser, findProduct);

        cartRepository.save(cart);
    }

    // 장바구니 수정 메서드
    @Transactional
    public void updateCart(CartUpdateRequestDto dto) throws AccessDeniedException{

        String email = getAuthenticatedUserEmail();
        checkQuantity(dto.getQuantity());
        Cart cart = cartRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다."));

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

        String email = getAuthenticatedUserEmail();
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("장바구니가 존재하지 않습니다!"));
        if(!email.equals(cart.getUser().getEmail())) {
            throw new AccessDeniedException("권한이 없습니다!");
        }

        cartRepository.delete(cart);
    }

    // 장바구니 단일 삭제 메서드
    @Transactional
    public void clearCart() {

        String email = getAuthenticatedUserEmail();
        List<Cart> cartList = cartRepository.findAllByUser_Email(email);
        cartRepository.deleteAll(cartList);
    }

    // 수정범위 확인 메서드
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
