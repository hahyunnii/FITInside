import React, { useState, useEffect, useRef } from 'react';
import DeliveryForm from "./DeliveryForm";
import '../cart/cart.css';
import './orderCreate.css';

const OrderCreate = () => {
    const [orderItems, setOrderItems] = useState([]); // 장바구니와 상품 정보
    const [deliveryFee, setDeliveryFee] = useState(0); // 배송비
    const [totalOriginalPrice, setTotalOriginalPrice] = useState(0); // 할인 전 총 금액
    const [totalDiscountedPrice, setTotalDiscountedPrice] = useState(0); // 할인된 총 금액
    const deliveryFormRef = useRef(null); // DeliveryForm을 참조

    // 장바구니 데이터와 배송비, 할인가격 로컬 스토리지에서 가져오기
    useEffect(() => {
        const storedOrderData = JSON.parse(localStorage.getItem('orderData')) || [];
        const storedShippingCost = JSON.parse(localStorage.getItem('shippingCost')) || 0;

        setOrderItems(storedOrderData); // 장바구니 데이터
        setDeliveryFee(storedShippingCost); // 배송비

        // 총 금액과 할인된 금액 계산
        const totalOriginal = storedOrderData.reduce((acc, item) => acc + item.originalTotalPrice, 0);
        const totalDiscounted = storedOrderData.reduce((acc, item) => acc + item.discountedTotalPrice, 0);

        setTotalOriginalPrice(totalOriginal);
        setTotalDiscountedPrice(totalDiscounted);
    }, []);

    const handleOrderSubmit = async () => {
        const deliveryData = deliveryFormRef.current.getFormData(); // DeliveryForm의 데이터 가져오기
        console.log("배송 정보: ", deliveryData);
        if (!deliveryData || Object.keys(deliveryData).length === 0) {
            alert('배송 정보를 입력해주세요.');
            return;
        }

        const { postalCode, deliveryAddress, detailedAddress, deliveryReceiver, deliveryPhone } = deliveryData;  // deliveryData를 분리

        submitOrder(postalCode, deliveryAddress, detailedAddress, deliveryReceiver, deliveryPhone);  // 개별 필드를 전달
    };

    const submitOrder = async (postalCode, deliveryAddress, detailedAddress, deliveryReceiver, deliveryPhone) => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/api/order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    postalCode,
                    deliveryAddress,
                    detailedAddress,
                    deliveryReceiver,
                    deliveryPhone,
                    orderItems,
                    deliveryFee,
                }),
            });
            const result = await response.json();
            alert('주문이 완료되었습니다.');
            localStorage.removeItem('orderData');
            localStorage.removeItem('shippingCost');
            localStorage.removeItem('localCart');
            localStorage.removeItem('dbCart');
            window.location.href = `/orders/${result.orderId}`;
        } catch (error) {
            console.error('주문이 실패했습니다:', error);
            alert('주문이 실패했습니다. 다시 시도해주세요.');
        }
    };

    const discountAmount = totalOriginalPrice - totalDiscountedPrice; // 할인 금액 계산
    const totalPayment = totalDiscountedPrice + deliveryFee; // 최종 결제 금액 계산

    return (
        <div className="container order-create-container my-5">
            <h2>주문서</h2>
            <br />
            <div className="both-container">
                <div className="left-container">
                    <table className="table">
                        <thead>
                        <tr>
                            <th>상품정보</th>
                            <th>상품금액</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orderItems.map((item, index) => (
                            <tr key={index}>
                                <td className="product-info">
                                    <div>
                                        <p className="product-name">{item.productName}</p>
                                        <p className="product-quantity">수량: {item.quantity}</p>
                                        {item.couponName && <p className="coupon-info">적용된 쿠폰 [{item.couponName}]</p>}
                                    </div>
                                </td>
                                <td className="product-price">
                                    {item.originalTotalPrice !== item.discountedTotalPrice ? (
                                        <div>
                                            <p style={{ textDecoration: 'line-through', marginBottom: '5px' }}>
                                                {item.originalTotalPrice.toLocaleString()} 원
                                            </p>
                                            <p style={{ color: '#B22222' }}>
                                                {item.discountedTotalPrice.toLocaleString()} 원
                                            </p>
                                        </div>
                                    ) : (
                                        <p>{item.originalTotalPrice.toLocaleString()} 원</p>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <DeliveryForm ref={deliveryFormRef} />
                </div>

                <div className="right-container">
                    <div className="order-summary-box">
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <p>총 상품 금액 ({orderItems.length})</p>
                            <p>{totalOriginalPrice.toLocaleString()} 원</p>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <p>할인 금액</p>
                            <p style={{ color: '#B22222' }}>- {discountAmount.toLocaleString()} 원</p>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <p>배송비</p>
                            <p>{deliveryFee === 0 ? '무료' : `${deliveryFee.toLocaleString()} 원`}</p>
                        </div>
                        <hr />
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <strong>총 결제 금액</strong>
                            <strong style={{ color: '#B22222' }}>{totalPayment.toLocaleString()} 원</strong>
                        </div>
                    </div>
                    <button className="btn-custom place-order-button" onClick={handleOrderSubmit}>
                        결제하기
                    </button>
                </div>
            </div>
        </div>
    );
};

export default OrderCreate;