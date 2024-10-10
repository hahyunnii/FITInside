import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

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

const OrderDetail = () => {
    const { orderId } = useParams(); // URL에서 orderId를 직접 가져오기
    const [orderDetail, setOrderDetail] = useState(null);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [deliveryAddress, setDeliveryAddress] = useState('');
    const [deliveryReceiver, setDeliveryReceiver] = useState('');
    const [deliveryPhone, setDeliveryPhone] = useState('');

    useEffect(() => {
        // 주문 상세 정보를 가져오는 API 호출
        const fetchOrderDetail = async () => {
            try {
                // 로컬 스토리지에서 토큰 가져오기
                const token = localStorage.getItem('token');

                // 주문 상세 정보
                const response = await axios.get(`http://localhost:8080/api/orders/${orderId}`, {
                    headers: {
                        Authorization: `Bearer ${token}` // 토큰을 인증 헤더에 추가
                    }
                });

                setOrderDetail(response.data);
                setDeliveryAddress(response.data.deliveryAddress);
                setDeliveryReceiver(response.data.deliveryReceiver);
                setDeliveryPhone(response.data.deliveryPhone);
            } catch (err) {
                console.error('주문 상세 정보 불러오기 실패:', err.response ? err.response.data : err.message);
                setError('주문 정보를 불러오는 중 오류가 발생했습니다.');
            }
        };

        fetchOrderDetail();
    }, [orderId]);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleSaveClick = async () => {
        try {
            const token = localStorage.getItem('token');
            const updatedOrderRequestDto = {
                deliveryAddress,
                deliveryReceiver,
                deliveryPhone,
            };

            const response = await axios.patch(`http://localhost:8080/api/orders/${orderId}`, updatedOrderRequestDto, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setOrderDetail(response.data);
            setIsEditing(false); // 수정 완료 후 편집 모드 종료
        } catch (err) {
            console.error('주문 수정 실패:', err.response ? err.response.data : err.message);
            alert('주문 수정에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleCancelClick = async () => {
        const confirmCancel = window.confirm('주문을 취소하시겠습니까?');
        if (!confirmCancel) {
            return;
        }

        try {
            const token = localStorage.getItem('token');

            await axios.delete(`http://localhost:8080/api/orders/${orderId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            // 주문 취소 후 다시 주문 상세 정보를 가져와서 갱신
            const response = await axios.get(`http://localhost:8080/api/orders/${orderId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setOrderDetail(response.data);
            alert('주문이 취소되었습니다.');
        } catch (err) {
            console.error('주문 취소 실패:', err.response ? err.response.data : err.message);
            alert('주문 취소에 실패했습니다. 다시 시도해주세요.');
        }
    };

    if (error) {
        return <div>{error}</div>;
    }

    if (!orderDetail) {
        return <div className="spinner">Loading...</div>;
    }

    return (
        <div>
            <h2>주문 상세 정보</h2>
            <p><strong>주문 번호:</strong> {orderDetail.orderId}</p>
            <p><strong>주문 날짜:</strong> {new Date(orderDetail.createdAt).toLocaleString()}</p>
            <p><strong>주문 상태:</strong> {getStatusLabel(orderDetail.orderStatus)}</p>
            <p><strong>총 가격:</strong> {orderDetail.totalPrice}원</p>
            <p><strong>배송비:</strong> {orderDetail.deliveryFee}원</p>

            {isEditing ? (
                <div>
                    <h3>배송 정보 수정</h3>
                    <label>
                        <strong>배송 주소:</strong>
                        <input
                            type="text"
                            value={deliveryAddress}
                            onChange={(e) => setDeliveryAddress(e.target.value)}
                        />
                    </label>
                    <br />
                    <label>
                        <strong>수령인:</strong>
                        <input
                            type="text"
                            value={deliveryReceiver}
                            onChange={(e) => setDeliveryReceiver(e.target.value)}
                        />
                    </label>
                    <br />
                    <label>
                        <strong>연락처:</strong>
                        <input
                            type="text"
                            value={deliveryPhone}
                            onChange={(e) => setDeliveryPhone(e.target.value)}
                        />
                    </label>
                    <br />
                    <button onClick={handleSaveClick}>저장</button>
                </div>
            ) : (
                <>
                    <p><strong>배송 주소:</strong> {orderDetail.deliveryAddress}</p>
                    <p><strong>수령인:</strong> {orderDetail.deliveryReceiver}</p>
                    <p><strong>연락처:</strong> {orderDetail.deliveryPhone}</p>
                    {getStatusLabel(orderDetail.orderStatus) === '주문 완료' && (
                        <>
                            <button onClick={handleEditClick}>수정</button>
                            <button onClick={handleCancelClick}>취소</button>
                        </>
                    )}
                </>
            )}

            <h3>주문 상품 목록</h3>
            <ul>
                {orderDetail.orderProducts.map((product) => (
                    <li key={product.productId}>
                        <p><strong>상품명:</strong> {product.orderProductName}</p>
                        <p><strong>가격:</strong> {product.orderProductPrice}원</p>
                        <p><strong>수량:</strong> {product.count}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default OrderDetail;