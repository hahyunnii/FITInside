import React, { useState, useEffect, useRef } from 'react';
import DeliveryForm from "./DeliveryForm";
import '../cart/cart.css';
import './orderCreate.css';

const OrderCreate = () => {
    const [orderItems, setOrderItems] = useState([]); // 장바구니와 상품 정보
    const [deliveryFee, setDeliveryFee] = useState(0); // 배송비
    const [totalOriginalPrice, setTotalOriginalPrice] = useState(0); // 할인 전 총 금액
    const [totalDiscountedPrice, setTotalDiscountedPrice] = useState(0); // 할인된 총 금액
    const [productDetails, setProductDetails] = useState({}); // 상품 이미지
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

        // 각 상품의 이미지 정보 가져오기
        const fetchProductDetails = async () => {
            const details = {};

            for (const item of storedOrderData) {
                try {
                    const response = await fetch(`http://localhost:8080/api/products/${item.productId}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`, // Authorization 헤더 추가
                            'Content-Type': 'application/json'
                        }
                    });

                    const productData = await response.json();
                    details[item.productId] = productData; // 상품 상세 정보를 저장
                } catch (error) {
                    console.error('상품 조회 실패', error);
                }
            }

            setProductDetails(details); // 상태 업데이트
        }

        if (storedOrderData.length > 0) {
            fetchProductDetails();
        }
    }, []);

    const handleOrderSubmit = async () => {
        const deliveryData = deliveryFormRef.current.getFormData(); // DeliveryForm의 데이터 가져오기
        console.log("배송 정보: ", deliveryData);
        if (!deliveryData || Object.keys(deliveryData).length === 0) {
            alert('배송 정보를 입력해주세요.');
            return;
        }

        const { postalCode, deliveryAddress, detailedAddress, deliveryMemo, deliveryReceiver, deliveryPhone } = deliveryData;  // deliveryData를 분리

        submitOrder(postalCode, deliveryAddress, detailedAddress, deliveryMemo, deliveryReceiver, deliveryPhone);  // 개별 필드를 전달
    };

    const submitOrder = async (postalCode, deliveryAddress, detailedAddress, deliveryMemo, deliveryReceiver, deliveryPhone) => {
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
                    deliveryMemo,
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
                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                        {productDetails[item.productId] && productDetails[item.productId].productImgUrls && (
                                            <img
                                                style={{ width: '100px', height: '100px', marginRight: '10px' }}
                                                src={productDetails[item.productId].productImgUrls[0]}
                                                alt={item.productName}
                                            />
                                        )}
                                        <div>
                                            <p className="product-name">{item.productName}</p>
                                            <p className="product-quantity">수량: {item.quantity}</p>
                                            {item.couponName && <p className="coupon-info">적용된 쿠폰 [{item.couponName}]</p>}
                                        </div>
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