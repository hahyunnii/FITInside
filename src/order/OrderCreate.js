import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import DeliveryForm from "./DeliveryForm";
import '../cart/cart.css';
import './orderCreate.css';

const OrderCreate = () => {
    const [cartItems, setCartItems] = useState([]); // 장바구니와 상품 정보
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const deliveryFormRef = useRef(null); // DeliveryForm을 참조

    // 장바구니 데이터와 상품 정보를 가져오기
    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('http://localhost:8080/api/order', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const cartData = response.data.cartProducts || []; // 장바구니 + 상품 정보
                setCartItems(cartData); // cartProducts 리스트
                setLoading(false);
            } catch (error) {
                console.error('장바구니 및 상품 데이터를 가져오는 데 실패했습니다:', error);
                setError('장바구니 및 상품 데이터를 가져오는 중 오류가 발생했습니다.');
                setLoading(false);
            }
        };

        fetchCartItems();
    }, []);

    const handleOrderSubmit = async () => {
        const deliveryData = deliveryFormRef.current.getFormData(); // DeliveryForm의 데이터 가져오기
        if (!deliveryData || Object.keys(deliveryData).length === 0) {
            alert('배송 정보를 입력해주세요.');
            return;
        }
        submitOrder(deliveryData);
    };

    const submitOrder = async (deliveryData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post('http://localhost:8080/api/order', deliveryData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            alert('주문이 완료되었습니다.');
            localStorage.removeItem('localCart');
            localStorage.removeItem('dbCart');
            window.location.href = `/orders/${response.data.orderId}`;
        } catch (error) {
            console.error('주문이 실패했습니다:', error);
            alert('주문이 실패했습니다. 다시 시도해주세요.');
        }
    };

    if (loading) {
        return <div className="spinner">Loading...</div>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    const totalOrderPrice = cartItems.reduce((total, item) => total + item.quantity * item.price, 0);
    const deliveryFee = totalOrderPrice >= 20000 ? 0 : 2500;
    const totalPayment = totalOrderPrice + deliveryFee;

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
                        {cartItems.map((item, index) => (
                            <tr key={index}>
                                <td className="product-info">
                                    <div>
                                        <p className="product-name">{item.productName}</p>
                                        <p className="product-quantity">수량: {item.quantity}</p>
                                    </div>
                                </td>
                                <td className="product-price">
                                    {(item.price * item.quantity).toLocaleString()} 원
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
                            <p>총 상품 금액 ({cartItems.length})</p>
                            <p>{totalOrderPrice.toLocaleString()} 원</p>
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