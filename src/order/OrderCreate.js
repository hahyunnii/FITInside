import React, { useState, useEffect } from 'react';
import axios from 'axios';
import DeliveryForm from "./DeliveryForm";

const OrderCreate = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 장바구니 데이터와 상품 정보를 가져오기
    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const token = localStorage.getItem('token');
                // 장바구니 항목 가져오기
                const cartResponse = await axios.get('http://localhost:8080/api/carts', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const cartData = cartResponse.data.carts || []; // carts 리스트 가져오기

                const updatedCartItems = await Promise.all(
                    cartData.map(async (item) => {
                        // 각 상품의 상세 정보를 가져오기
                        const productResponse = await axios.get(`http://localhost:8080/api/products/${item.productId}`, {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                        });

                        return {
                            ...item,
                            name: productResponse.data.productName, // 상품 이름
                            price: productResponse.data.price,     // 상품 가격
                        };
                    })
                );

                setCartItems(updatedCartItems); // 업데이트된 장바구니 항목 설정
                setLoading(false);
            } catch (error) {
                console.error('장바구니 데이터를 가져오는 데 실패했습니다:', error);
                setError('장바구니 데이터를 가져오는 중 오류가 발생했습니다.');
                setLoading(false);
            }
        };

        fetchCartItems();
    }, []);

    const handleOrderSubmit = async (deliveryData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post('http://localhost:8080/api/orders', deliveryData, {
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

    return (
        <div>
            <h1>Order Page</h1>
            <ul>
                {cartItems.map((item) => (
                    <li key={item.productId}>
                        {item.name} - {item.quantity} 개 * {item.price} 원 = {item.quantity * item.price} 원
                    </li>
                ))}
            </ul>
            <DeliveryForm onSubmit={handleOrderSubmit} />
        </div>
    );
};

export default OrderCreate;