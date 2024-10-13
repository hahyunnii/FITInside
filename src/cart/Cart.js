import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getCart, removeFromCart, clearCart, fetchProduct, useCartCount, updateCartQuantity } from './cartStorage';
import AvailableCouponModal from '../coupon/AvailableCouponModal';
import './cart.css';

const Cart = () => {
    const [cart, setCart] = useState([]);
    const [productDetails, setProductDetails] = useState({});
    const [selectedItems, setSelectedItems] = useState(new Set());
    const [showCouponModal, setShowCouponModal] = useState(false);
    const [currentProductCoupons, setCurrentProductCoupons] = useState([]);
    const [currentProduct, setCurrentProduct] = useState(null);
    const [totalDiscount, setTotalDiscount] = useState(0);
    const [appliedCoupons, setAppliedCoupons] = useState([]);

    useEffect(() => {
        const cartData = getCart();
        setCart(cartData);
    }, []);

    useEffect(() => {
        if (cart.length > 0) {
            const fetchAllProducts = async () => {
                const details = {};
                for (const item of cart) {
                    const productData = await fetchProduct(item.id);
                    if (productData) {
                        details[item.id] = productData;
                    }
                }
                setProductDetails(details);
            };

            fetchAllProducts();
        }
    }, [cart]);

    const handleRemoveFromCart = (id) => {
        removeFromCart(id);
        setCart(getCart());
        selectedItems.delete(id);
        setSelectedItems(new Set(selectedItems));
        setAppliedCoupons(prevCoupons => prevCoupons.filter(c => c.productId !== id));
    };

    const handleClearCart = () => {
        clearCart();
        setCart([]);
        setProductDetails({});
        setSelectedItems(new Set());
        setTotalDiscount(0);
        setAppliedCoupons([]); // 전체 삭제 시 적용된 쿠폰 초기화
    };

    const handleSelectAll = () => {
        if (selectedItems.size === cart.length) {
            setSelectedItems(new Set());
        } else {
            const allIds = new Set(cart.map(item => item.id));
            setSelectedItems(allIds);
        }
    };

    const handleRemoveSelected = () => {
        selectedItems.forEach(id => {
            removeFromCart(id);
        });
        setCart(getCart());
        setSelectedItems(new Set());
    };

    const handleQuantityChange = (id, newQuantity) => {
        updateCartQuantity(id, newQuantity);
        setCart(getCart());
    };

    const cartCount = useCartCount();

    const fetchAvailableCoupons = async (productId) => {
        const response = await fetch(`http://localhost:8080/api/coupons/${productId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
        });

        if (!response.ok) {
            throw new Error('적용 가능 쿠폰 목록을 가져오는 데 실패했습니다.');
        }

        const data = await response.json();
        setCurrentProductCoupons(data.coupons);
    };

    const handleShowCouponModal = (item) => {
        fetchAvailableCoupons(item.id);
        setCurrentProduct(item);
        setShowCouponModal(true);
    };

    const handleCloseCouponModal = () => {
        setShowCouponModal(false);
        setCurrentProduct(null);
    };

    const handleApplyCoupon = (coupon) => {
        const currentPrice = productDetails[currentProduct.id]?.price || 0;
        const discountAmount = coupon.type === 'AMOUNT' ? coupon.value : (currentPrice * coupon.percentage) / 100;

        const duplicateCoupon = appliedCoupons.find(c => c.couponMemberId === coupon.couponMemberId && c.productId !== currentProduct.id);
        let updatedCoupons = [...appliedCoupons];

        if (duplicateCoupon) {
            updatedCoupons = updatedCoupons.filter(c => c.couponMemberId !== coupon.couponMemberId);
            alert(`쿠폰 ${duplicateCoupon.name}이 다른 상품에 적용되어 있어 제거되었습니다.`);
        }

        const existingCoupon = updatedCoupons.find(c => c.productId === currentProduct.id);
        if (existingCoupon) {
            updatedCoupons = updatedCoupons.filter(c => c.productId !== currentProduct.id);
            alert(`쿠폰 ${existingCoupon.name}이 현재 상품에 적용되어 있어 제거되었습니다.`);
        }

        updatedCoupons.push({ ...coupon, productId: currentProduct.id });
        setAppliedCoupons(updatedCoupons);

        const newTotalDiscount = updatedCoupons.reduce((total, c) => {
            const price = productDetails[c.productId]?.price || 0;
            const discount = c.type === 'AMOUNT' ? c.value : (price * c.percentage) / 100;
            return total + discount;
        }, 0);

        setTotalDiscount(newTotalDiscount);
        handleCloseCouponModal();
    };

    const getTotalPrice = () => {
        const subtotal = cart.reduce((total, item) => {
            const price = productDetails[item.id]?.price || 0;
            return total + price * item.quantity;
        }, 0);

        return subtotal - totalDiscount; // 할인 적용 후 최종 금액
    };

    const handleOrder = () => {
        const orderData = cart.map(item => {
            const productDetail = productDetails[item.id];
            const appliedCoupon = appliedCoupons.find(coupon => coupon.productId === item.id);
            const itemPrice = productDetail.price;
            const discountAmount = appliedCoupon ? (appliedCoupon.type === 'AMOUNT' ? appliedCoupon.value : (itemPrice * appliedCoupon.percentage) / 100) : 0;
            const originalTotalPrice = itemPrice * item.quantity;
            const discountedTotalPrice = originalTotalPrice - discountAmount;

            return {
                productId: item.id,
                productName: productDetail.productName,
                quantity: item.quantity,
                itemPrice,
                originalTotalPrice,
                discountedTotalPrice,
                couponName: appliedCoupon ? appliedCoupon.name : null,
                couponMemberId: appliedCoupon ? appliedCoupon.couponMemberId : null,
            };
        });

        localStorage.setItem('orderData', JSON.stringify(orderData));
        localStorage.setItem('shippingCost', JSON.stringify(shippingCost));
    };

    const calculateShippingCost = () => {
        const totalPrice = getTotalPrice();
        if (cartCount === 0) {
            return 0;
        }
        return totalPrice < 20000 ? 2500 : 0;
    };

    const shippingCost = calculateShippingCost();

    return (
        <div className="cart-container">
            <h2>쇼핑백</h2>
            <br />
            <div className="both-container">
                <div className="left-container">
                    <div className="d-flex justify-content-between">
                        <div>
                            <button className="btn btn-light text-dark me-2" style={{ border: '1px solid #ced4da' }} onClick={handleSelectAll}>
                                {selectedItems.size === cart.length ? '전체선택 해제' : '전체선택'}
                            </button>
                            <button className="btn btn-light text-dark me-2" style={{ border: '1px solid #ced4da' }} onClick={handleRemoveSelected} disabled={selectedItems.size === 0}>
                                선택삭제
                            </button>
                            <button className="btn btn-danger" style={{ border: '1px solid #dc3545' }} onClick={handleClearCart}>
                                전체삭제
                            </button>
                        </div>
                    </div>
                    <hr />

                    {cart.length === 0 ? (
                        <p className="no-content">장바구니에 상품이 없습니다.</p>
                    ) : (
                        <table className="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th>상품정보</th>
                                <th>상품금액</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {cart.map(item => (
                                <tr key={item.id}>
                                    <td>
                                        <input
                                            type="checkbox"
                                            checked={selectedItems.has(item.id)}
                                            onChange={() => {
                                                const newSelectedItems = new Set(selectedItems);
                                                if (newSelectedItems.has(item.id)) {
                                                    newSelectedItems.delete(item.id);
                                                } else {
                                                    newSelectedItems.add(item.id);
                                                }
                                                setSelectedItems(newSelectedItems);
                                            }}
                                        />
                                    </td>
                                    <td>
                                        {productDetails[item.id] ? (
                                            <div>
                                                <p style={{ margin: `0` }}>{productDetails[item.id].manufacturer}</p>
                                                <p style={{ fontWeight: 'bold' }}>
                                                    <Link to={`/product/${item.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                                        {productDetails[item.id].productName}
                                                    </Link>
                                                </p>
                                                <label className="d-flex align-items-center">
                                                    수량
                                                    <select
                                                        className="form-select ms-2"
                                                        style={{ width: 'auto' }}
                                                        value={item.quantity}
                                                        onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value, 10))}
                                                    >
                                                        {[...Array(20)].map((_, index) => (
                                                            <option key={index + 1} value={index + 1}>
                                                                {index + 1}
                                                            </option>
                                                        ))}
                                                    </select>
                                                </label>
                                                {/* 현재 적용된 쿠폰 및 할인 금액 표시 */}
                                                {appliedCoupons.filter(coupon => coupon.productId === item.id).map((coupon, index) => {
                                                    const discountAmount = coupon.type === 'AMOUNT' ? coupon.value : (productDetails[item.id].price * coupon.percentage) / 100;
                                                    return (
                                                        <p key={index} style={{ margin: '0', color: '#629a72' }}>
                                                            적용된 쿠폰: {coupon.name} (-{discountAmount.toLocaleString()} 원)
                                                        </p>
                                                    );
                                                })}
                                            </div>
                                        ) : (
                                            <p>상품 정보를 불러올 수 없습니다...</p>
                                        )}
                                    </td>
                                    <td>
                                        {productDetails[item.id] ? (
                                            <div>
                                                <p>
                                                    {(productDetails[item.id].price * item.quantity).toLocaleString()} 원
                                                </p>
                                                <button className="btn btn-light text-dark me-2" style={{ border: '1px solid #ced4da' }} onClick={() => handleShowCouponModal(item)}>
                                                    적용 가능 쿠폰
                                                </button>
                                            </div>
                                        ) : (
                                            <p>상품 정보를 불러올 수 없습니다...</p>
                                        )}
                                    </td>
                                    <td>
                                        <button className="btn btn-light text-dark me-2" style={{ border: '1px solid #ced4da' }} onClick={() => handleRemoveFromCart(item.id)}>
                                            삭제
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>

                <div className="right-container">
                    <div className="order-container">
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <p>상품 금액 ({cartCount})</p>
                            {totalDiscount > 0 ? (
                                <span>
                                    <p style={{ textDecoration: 'line-through', marginRight: '8px' }}>
                                        {(getTotalPrice() + totalDiscount).toLocaleString()} 원
                                    </p>
                                    <p style={{ color: '#B22222' }}>
                                        {getTotalPrice().toLocaleString()} 원
                                    </p>
                                </span>
                            ) : (
                                <p>{getTotalPrice().toLocaleString()} 원</p>
                            )}
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <p>배송비</p>
                            {cartCount === 0 ? (
                                <p>0 원</p>
                            ) : shippingCost > 0 ? (
                                <p>{shippingCost.toLocaleString()} 원</p>
                            ) : (
                                <span>
                                    <p style={{ textDecoration: 'line-through' }}>2,500 원</p>
                                    <p style={{ marginLeft: '8px' }}>무료</p>
                                </span>
                            )}
                        </div>
                        <hr />
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <strong>결제예정금액</strong>
                            {cartCount === 0 ? (
                                <strong style={{ color: '#B22222' }}>0 원</strong>
                            ) : (
                                <strong style={{ color: '#B22222' }}>{(getTotalPrice() + shippingCost).toLocaleString()} 원</strong>
                            )}
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'center' }}>
                            <div className="discount-container">
                                <strong>{totalDiscount.toLocaleString()} 원</strong>
                                <strong style={{ color: '#B22222' }}>
                                    {getTotalPrice() > 0 ? `(${Math.floor((totalDiscount / (getTotalPrice() + totalDiscount)) * 100)}%)` : '(0%)'}
                                </strong>
                                &nbsp;<p style={{ margin: 0 }}>할인 받았어요!</p>
                            </div>
                        </div>
                        {/* 적용된 쿠폰 정보 출력 */}
                        {appliedCoupons.length > 0 && (
                            <div style={{ display: 'flex', justifyContent: 'space-between', margin: '10px 0' }}>
                                <div className="applied-coupon-container">
                                    <strong>적용된 쿠폰</strong>
                                    {appliedCoupons.map((coupon, index) => (
                                        <p key={index} style={{ color: '#629a72', margin: 0 }}>
                                            {coupon.name} {coupon.type === 'AMOUNT' ? `(-${coupon.value.toLocaleString()} 원)` : `(${coupon.percentage}% 할인)`}
                                        </p>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'center', margin: `10px 0` }}>
                        <a className="btn btn-light text-dark me-2 p-3" style={{ border: '1px solid #ced4da' }} href="/">
                            계속 쇼핑하기
                        </a>
                        <a
                            className="btn btn-custom p-3"
                            onClick={handleOrder}
                            href={cartCount > 0 ? "/order" : "/cart"} // cartCount가 0일 때 링크를 비활성화
                            style={{
                                pointerEvents: cartCount === 0 ? 'none' : 'auto',
                                opacity: cartCount === 0 ? 0.5 : 1,
                                fontSize: '16px'
                            }} // 비활성화 스타일
                        >전체 주문하기
                        </a>
                    </div>
                </div>
            </div>

            {/* 쿠폰 모달 */}
            {showCouponModal && currentProduct && (
                <AvailableCouponModal
                    coupons={currentProductCoupons}
                    onClose={handleCloseCouponModal}
                    onApplyCoupon={handleApplyCoupon}
                />
            )}
        </div>
    );
};

export default Cart;
