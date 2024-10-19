import React from 'react';
import './availableCouponModal.css';
import './coupon.css';

const AvailableCouponModal = ({ coupons, onClose, onApplyCoupon }) => {
    // 모달 외부 클릭 시 닫기
    const handleModalClick = (e) => {
        // 클릭한 요소가 모달 콘텐츠가 아닐 경우 모달을 닫음
        if (e.target.classList.contains('modal')) {
            onClose();
        }
    };

    const handleApply = (coupon) => {
        onApplyCoupon(coupon); // 쿠폰을 부모 컴포넌트로 전달
        onClose(); // 모달 닫기
    };

    return (
        <div className="modal" onClick={handleModalClick}> {/* 모달 외부 클릭 이벤트 추가 */}
            <div className="modal-content">
                <div className="d-flex justify-content-between">
                    <h3>적용 가능한 쿠폰</h3>
                    <span className="close" onClick={onClose}>&times;</span>
                </div>

                {Array.isArray(coupons) && coupons.length > 0 ? (
                    coupons.map((coupon, index) => (
                        <div className="couponWrap d-flex justify-content-center" key={coupon.id ? coupon.id : index}> {/* 고유한 key prop 사용 */}
                            <div
                                className={`coupon couponLeft ${coupon.type === 'AMOUNT' ? 'red' : 'blue'}`}>
                                <h1 className="m-0" style={{ color: "white" }}>
                                    {coupon.name}
                                </h1>
                                <div className="title mt-4 mb-2">
                                    <strong>{coupon.code}</strong>
                                </div>
                                <div className="name mb-0">
                                    <strong>{coupon.categoryName}</strong>
                                </div>
                                <div className="name m-0">
                                    ({coupon.type === 'AMOUNT' ? '고정 금액' : '퍼센티지'})&nbsp;
                                    <strong>{coupon.type === 'AMOUNT' ? `${new Intl.NumberFormat().format(coupon.value)}원` : `${coupon.percentage}%`}</strong> 할인
                                </div>
                                <div className="name m-0">
                                    {coupon.minValue === 0 ? '최소 주문 금액 없음' : new Intl.NumberFormat().format(coupon.minValue) + ' 원 부터 적용 가능'}
                                </div>
                                <div className="name m-0">
                                    ~ {coupon.expiredAt} 까지 적용 가능
                                </div>
                            </div>

                            <div className={`coupon couponRight ${coupon.type === 'AMOUNT' ? 'red' : 'blue'}`}>
                                <h1 className="m-0" style={{color: "white"}}>적용</h1>
                                <div className="mt-5 d-flex flex-column justify-content-center align-items-center">
                                    <button
                                        className="btn btn-light mb-2"
                                        style={{
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            height: '60px',
                                            width: '60px'
                                        }}
                                        onClick={() => handleApply(coupon)} // 버튼 클릭 시 쿠폰 입력 요청
                                    >
                                        <span className="material-icons">check_circle</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>적용 가능한 쿠폰이 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default AvailableCouponModal;
