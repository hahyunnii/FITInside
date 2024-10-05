import { useState, useEffect } from 'react';
const CART_STORAGE_KEY = 'guestCart';

export const useCartCount = () => {
    const [cartCount, setCartCount] = useState(0);

    useEffect(() => {
        const updateCartCount = () => {
            const storedCart = JSON.parse(localStorage.getItem(CART_STORAGE_KEY)) || [];
            setCartCount(storedCart.length); // 장바구니 품목 개수 설정
        };

        // 초기 카운트 업데이트
        updateCartCount();

        // storage 이벤트 리스너 추가
        window.addEventListener('storage', updateCartCount);

        // 컴포넌트 언마운트 시 이벤트 리스너 제거
        return () => {
            window.removeEventListener('storage', updateCartCount);
        };
    }, []);

    return cartCount;
};

// 장바구니 가져오기
export const getCart = () => {
    const cart = localStorage.getItem(CART_STORAGE_KEY);
    return cart ? JSON.parse(cart) : [];
};

// 장바구니 추가하기
export const addToCart = (item) => {
    const cart = getCart();
    const existingItemIndex = cart.findIndex(cartItem => cartItem.productId === item.productId);

    if (existingItemIndex >= 0) {
        // 이미 장바구니에 있는 경우 메시지 출력하고 추가하지 않음
        alert("이미 장바구니에 있습니다."); // 사용자에게 알림
        return false; // 추가하지 않음
    } else {
        // 새 아이템 추가
        item.quantity = item.quantity || 1; // 기본 수량 1로 설정
        cart.push(item);
    }

    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart));
    // 카운트 업데이트
    window.dispatchEvent(new Event('storage')); // storage 이벤트 발생

    return true; // 성공적으로 추가됨
};


// 장바구니 수량 업데이트하기
export const updateCartQuantity = (id, quantity) => {
    const cart = getCart();
    const updatedCart = cart.map(item => {
        if (item.id === id) {
            return { ...item, quantity }; // 수량 업데이트
        }
        return item;
    });

    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(updatedCart));
    // 카운트 업데이트
    window.dispatchEvent(new Event('storage')); // storage 이벤트 발생
};

// 장바구니 삭제하기
export const removeFromCart = (id) => {
    const cart = getCart();
    const updatedCart = cart.filter(item => item.id !== id);
    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(updatedCart));
    // 카운트 업데이트
    window.dispatchEvent(new Event('storage')); // storage 이벤트 발생
};

// 장바구니 초기화하기
export const clearCart = () => {
    localStorage.removeItem(CART_STORAGE_KEY);
    // 카운트 업데이트
    window.dispatchEvent(new Event('storage')); // storage 이벤트 발생
};

// 상품 데이터를 가져오는 함수
export const fetchProduct = async (id) => {
    try {
        const response = await fetch(`http://localhost:8080/api/products/${id}`);
        if (!response.ok) {
            throw new Error('네트워크 응답이 좋지 않습니다.');
        }
        const productData = await response.json();
        return productData;
    } catch (error) {
        console.error('상품 가져오기 오류:', error);
        return null; // 오류가 발생하면 null 반환
    }
};
