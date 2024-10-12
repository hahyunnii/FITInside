import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './orderAdminList.css';

const OrderAdminList = () => {
    const [orders, setOrders] = useState([]);
    const [pendingStatusChanges, setPendingStatusChanges] = useState({});
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [error, setError] = useState(null);

    // 관리자의 전체 주문 정보를 가져오는 API 호출
    const fetchAdminOrders = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get('http://localhost:8080/api/admin/orders', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setOrders(response.data.orders);
            setTotalPages(response.data.totalPages);
        } catch (err) {
            console.error('전체 주문 정보 불러오기 실패:', err.response ? err.response.data : err.message);
            setError('주문 목록을 불러오는 중 오류가 발생했습니다.');
        }
    };

    useEffect(() => {
        fetchAdminOrders(currentPage);
    }, [currentPage]);

    // 상태 변경을 임시로 저장
    const handleStatusChange = (orderId, newStatus) => {
        setPendingStatusChanges((prevChanges) => ({
            ...prevChanges,
            [orderId]: newStatus
        }));
    };

    const handleSaveStatusChange = async (orderId) => {
        const newStatus = pendingStatusChanges[orderId];

        // 만약 새로운 상태가 존재하지 않거나, 기존 상태와 동일한 경우 경고 메시지 표시
        const order = orders.find((o) => o.orderId === orderId);
        if (!newStatus || order.orderStatus === newStatus) {
            alert('변경된 값이 없습니다.');
            return;
        }

        try {
            const token = localStorage.getItem('token');
            const requestData = { status: newStatus };

            const response = await axios.patch(`http://localhost:8080/api/admin/orders/${orderId}/status`, requestData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            // 서버로부터 받은 새로운 주문 상태를 즉시 반영
            setOrders((prevOrders) =>
                prevOrders.map((order) =>
                    order.orderId === orderId ? { ...order, orderStatus: response.data.orderStatus } : order
                )
            );

            alert('주문 상태가 성공적으로 수정되었습니다.');
            setPendingStatusChanges((prevChanges) => {
                const updatedChanges = { ...prevChanges };
                delete updatedChanges[orderId];
                return updatedChanges;
            });
            window.location.reload();
        } catch (err) {
            console.error('주문 상태 변경 실패:', err.response ? err.response.data : err.message);
            alert('주문 상태 변경에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleDeleteOrder = async (orderId) => {
        const confirmDelete = window.confirm('주문을 삭제하시겠습니까?');
        if (!confirmDelete) {
            return;
        }

        try {
            const token = localStorage.getItem('token');
            await axios.delete(`http://localhost:8080/api/admin/orders/${orderId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            alert('주문이 성공적으로 삭제되었습니다.');
            fetchAdminOrders(currentPage);
        } catch (err) {
            console.error('주문 삭제 실패:', err.response ? err.response.data : err.message);
            alert('주문 삭제에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    if (error) {
        return <div>{error}</div>;
    }

    if (!orders || orders.length === 0) {
        return <div>주문 목록이 없습니다.</div>;
    }

    const statusOptions = [
        { value: 'ORDERED', label: '주문 완료' },
        { value: 'COMPLETED', label: '배송 완료' },
        { value: 'CANCELLED', label: '주문 취소' }
    ];

    return (
        <div className="order-admin-container">
            <h2 className="order-admin-title">전체 주문 목록</h2>
            <table className="order-admin-table">
                <thead>
                <tr>
                    <th>주문 날짜</th>
                    <th>배송 주소</th>
                    <th>총 가격</th>
                    <th>주문 상태</th>
                    <th>액션</th>
                </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr key={order.orderId}>
                            <td>{new Date(order.createdAt).toLocaleString()}</td>
                            <td>{order.deliveryAddress}</td>
                            <td>{(order.totalPrice).toLocaleString()}원</td>
                            <td>
                                <select
                                    value={pendingStatusChanges[order.orderId] || order.orderStatus}
                                    onChange={(e) => handleStatusChange(order.orderId, e.target.value)}
                                >
                                    {statusOptions.map((option) => (
                                        <option key={option.value} value={option.value}>
                                            {option.label}
                                        </option>
                                    ))}
                                </select>
                            </td>
                            <td>
                                <button className="action-button" onClick={() => handleSaveStatusChange(order.orderId)}>수정</button>
                                <button className="action-button delete-button" onClick={() => handleDeleteOrder(order.orderId)}>삭제</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

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

export default OrderAdminList;