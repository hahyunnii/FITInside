import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './orderList.css';

const statusOptions = [
    { value: 'ORDERED', label: '주문 완료' },
    { value: 'COMPLETED', label: '배송 완료' },
    { value: 'CANCELLED', label: '주문 취소' }
];

// 주문 상태를 한글로 변환
const getStatusLabel = (statusValue) => {
    const status = statusOptions.find(option => option.value === statusValue);
    return status ? status.label : statusValue;
};

const OrderList = () => {
    const [orders, setOrders] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchOrders(currentPage);
    }, [currentPage]);

    const fetchOrders = async (page) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get(`http://localhost:8080/api/orders?page=${page}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setOrders(response.data.orders);
            setTotalPages(response.data.totalPages);
        } catch (err) {
            console.error('주문 목록 불러오기 실패:', err.response ? err.response.data : err.message);
            setError('주문 목록을 불러오는 중 오류가 발생했습니다.');
        }
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    if (error) {
        return <div>{error}</div>;
    }

    if (!orders.length) {
        return <div>주문 목록이 없습니다.</div>;
    }

    return (
        <div className="container my-4">
            <h2 className="text-center mb-4">주문 내역</h2>
            <div className="order-card-list">
                {orders.map((order) => (
                    <div key={order.orderId} className="order-card card shadow-sm mb-4">
                        <div className="card-header">
                            {getStatusLabel(order.orderStatus)}
                        </div>
                        <div className="card-body">
                            <p className="card-text"><strong>주문 상품:</strong> {order.productNames.join(', ')}</p>
                            <p className="card-text"><strong>총 가격:</strong> {(order.totalPrice).toLocaleString()}원</p>
                            <p className="card-text"><strong>주문 날짜:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
                        </div>
                        <div className="card-footer bg-light">
                            <p className="mb-1"><strong>배송 주소:</strong> {order.deliveryAddress}</p>
                            <Link to={`/orders/${order.orderId}`} className="btn btn-primary btn-sm d-block mt-2">
                                주문 상세 보기
                            </Link>
                        </div>
                    </div>
                ))}
            </div>

            <div className="pagination">
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index}
                        className={`pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                        onClick={() => handlePageChange(index + 1)}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default OrderList;