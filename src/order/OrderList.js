import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const OrderList = () => {
    const [orders, setOrders] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        // 전체 주문 정보를 가져오는 API 호출
        const fetchOrders = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('http://localhost:8080/api/orders', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                setOrders(response.data);
            } catch (err) {
                console.error('전체 주문 정보 불러오기 실패:', err.response ? err.response.data : err.message);
                setError('주문 목록을 불러오는 중 오류가 발생했습니다.');
            }
        };

        fetchOrders();
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    if (!orders.length) {
        return <div>주문 목록이 없습니다.</div>;
    }

    return (
        <div>
            <h2>전체 주문 목록</h2>
            <ul>
                {orders.map((order) => (
                    <li key={order.orderId}>
                        <Link to={`/orders/${order.orderId}`}>
                            <div>
                                <p><strong>주문 번호:</strong> {order.orderId}</p>
                                <p><strong>주문 상태:</strong> {order.orderStatus}</p>
                                <p><strong>총 가격:</strong> {order.totalPrice}원</p>
                                <p><strong>배송 주소:</strong> {order.deliveryAddress}</p>
                                <p><strong>주문 상품:</strong> {order.productNames.join(', ')}</p>
                                <p><strong>주문 날짜:</strong> {new Date(order.createdAt).toLocaleString()}</p>
                            </div>
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default OrderList;