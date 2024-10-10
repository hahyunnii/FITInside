import React, { useEffect, useState } from 'react';
import axios from 'axios';

const OrderAdminList = () => {
    const [orders, setOrders] = useState([]);
    const [error, setError] = useState(null);
    const [pendingStatusChanges, setPendingStatusChanges] = useState({});

    // 관리자의 전체 주문 정보를 가져오는 API 호출
    const fetchAdminOrders = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get('http://localhost:8080/api/admin/orders', {
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

    useEffect(() => {
        // 컴포넌트가 마운트될 때 주문 목록을 불러옴
        fetchAdminOrders();
    }, []);

    const handleStatusChange = (orderId, newStatus) => {
        // 상태 변경을 임시로 저장
        setPendingStatusChanges((prevChanges) => ({
            ...prevChanges,
            [orderId]: newStatus
        }));
    };

    const handleSaveStatusChange = async (orderId) => {
        const newStatus = pendingStatusChanges[orderId];
        if (!newStatus) {
            alert('변경된 상태가 없습니다.');
            return;
        }

        try {
            const token = localStorage.getItem('token');
            const requestData = { status: newStatus };

            await axios.patch(`http://localhost:8080/api/admin/orders/${orderId}/status`, requestData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            // 상태 업데이트 후 주문 목록 갱신
            setOrders((prevOrders) =>
                prevOrders.map((order) =>
                    order.orderId === orderId ? { ...order, orderStatus: newStatus } : order
                )
            );

            alert('주문 상태가 성공적으로 수정되었습니다.');
            setPendingStatusChanges((prevChanges) => {
                const updatedChanges = { ...prevChanges };
                delete updatedChanges[orderId];
                return updatedChanges;
            });
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

            // 삭제 성공 시 알림창 표시 및 목록 새로고침
            alert('주문이 성공적으로 삭제되었습니다.');
            await fetchAdminOrders(); // 최신 주문 목록 불러오기
        } catch (err) {
            console.error('주문 삭제 실패:', err.response ? err.response.data : err.message);
            alert('주문 삭제에 실패했습니다. 다시 시도해주세요.');
        }
    };

    if (error) {
        return <div>{error}</div>;
    }

    if (!orders.length) {
        return <div>주문 목록이 없습니다.</div>;
    }

    const statusOptions = [
        { value: 'ORDERED', label: '주문 완료' },
        { value: 'COMPLETED', label: '배송 완료' },
        { value: 'CANCELLED', label: '주문 취소' }
    ];

    return (
        <div>
            <h2>관리자용 전체 주문 목록</h2>
            <table>
                <thead>
                <tr>
                    <th>주문 번호</th>
                    <th>주문 상태</th>
                    <th>총 가격</th>
                    <th>배송 주소</th>
                    <th>주문 날짜</th>
                    <th>액션</th>
                </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr key={order.orderId}>
                            <td>{order.orderId}</td>
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
                            <td>{order.totalPrice}원</td>
                            <td>{order.deliveryAddress}</td>
                            <td>{new Date(order.createdAt).toLocaleString()}</td>
                            <td>
                                <button onClick={() => handleSaveStatusChange(order.orderId)}>수정</button>
                                <button onClick={() => handleDeleteOrder(order.orderId)}>삭제</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default OrderAdminList;